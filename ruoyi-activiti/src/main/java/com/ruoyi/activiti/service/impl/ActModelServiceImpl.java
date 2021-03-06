package com.ruoyi.activiti.service.impl;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.ruoyi.activiti.domain.ModelEntityDto;
import com.ruoyi.activiti.service.ActModelService;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.core.text.Convert;
import org.activiti.bpmn.converter.BpmnXMLConverter;
import org.activiti.bpmn.model.BpmnModel;
import org.activiti.editor.language.json.converter.BpmnJsonConverter;
import org.activiti.engine.ActivitiException;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.repository.Model;
import org.activiti.engine.repository.ModelQuery;
import org.activiti.rest.editor.model.ModelEditorJsonRestResource;
import org.apache.batik.transcoder.TranscoderInput;
import org.apache.batik.transcoder.TranscoderOutput;
import org.apache.batik.transcoder.image.PNGTranscoder;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.*;

import static org.activiti.editor.constants.ModelDataJsonConstants.MODEL_ID;
import static org.activiti.editor.constants.ModelDataJsonConstants.MODEL_NAME;

@Service
public class ActModelServiceImpl implements ActModelService
{
    protected static final Logger LOGGER = LoggerFactory.getLogger(ModelEditorJsonRestResource.class);

    @Autowired
    private RepositoryService repositoryService;

    @Autowired
    private ObjectMapper objectMapper;

    /**
     * 查询模型列表
     * 
     * @param modelEntityDto 模型信息
     * @return 模型集合
     */
    @Override
    public TableDataInfo selectModelList(ModelEntityDto modelEntityDto)
    {
        TableDataInfo data = new TableDataInfo();
        ModelQuery modelQuery = repositoryService.createModelQuery();
        data.setTotal(modelQuery.count());
        data.setRows(modelQuery.orderByModelId().desc().listPage(modelEntityDto.getPageNum(), modelEntityDto.getPageSize()));
        return data;

    }

    /**
     * 创建模型
     * 
     * @param model 模型信息
     * @return 模型ID
     *
     */
    @Override
    public String createModel(Model model)
    {
        repositoryService.saveModel(model);
        String modelId = model.getId();
        // 完善ModelEditorSource
        ObjectNode editorNode = objectMapper.createObjectNode();
        editorNode.put("id", "canvas");
        editorNode.put("resourceId", "canvas");
        ObjectNode stencilSetNode = objectMapper.createObjectNode();
        stencilSetNode.put("namespace", "http://b3mn.org/stencilset/bpmn2.0#");
        editorNode.set("stencilset", stencilSetNode);
        try {
            repositoryService.addModelEditorSource(modelId, editorNode.toString().getBytes("utf-8"));
        }catch (UnsupportedEncodingException e){
            LOGGER.error("新建流程失败");
            throw  new ActivitiException("新建流程失败，不支持改编码，模型ID=" + model.getId(),e);
        }

        return modelId;
    }

    /**
     * 修改模型信息
     * 
     * @param model 模型信息
     * @param jsonXml json参数
     * @param svgXml xml参数
     */
    @Override
    public void update(Model model, String jsonXml, String svgXml)
    {
        try {
        repositoryService.saveModel(model);
        repositoryService.addModelEditorSource(model.getId(), jsonXml.getBytes("utf-8"));
        InputStream svgStream = new ByteArrayInputStream(svgXml.getBytes("utf-8"));
        TranscoderInput input = new TranscoderInput(svgStream);

        PNGTranscoder transcoder = new PNGTranscoder();
        ByteArrayOutputStream outStream = new ByteArrayOutputStream();
        TranscoderOutput output = new TranscoderOutput(outStream);

        transcoder.transcode(input, output);
        final byte[] result = outStream.toByteArray();
        repositoryService.addModelEditorSourceExtra(model.getId(), result);
        outStream.close();
        }catch (Exception e){
            LOGGER.error("保存模型失败");
            throw  new ActivitiException("保存模型失败，模型ID=" + model.getId(),e);

        }
    }

    /**
     * 查询模型编辑器
     * 
     * @param modelId 模型ID
     * @return json信息
     */
    @Override
    public ObjectNode selectWrapModelById(String modelId)
    {
        ObjectNode modelNode = null;
        Model model = repositoryService.getModel(modelId);
        if (model != null)
        {
            try
            {
                if (StringUtils.isNotEmpty(model.getMetaInfo()))
                {
                    modelNode = (ObjectNode) objectMapper.readTree(model.getMetaInfo());
                }
                else
                {
                    modelNode = objectMapper.createObjectNode();
                    modelNode.put(MODEL_NAME, model.getName());
                }
                modelNode.put(MODEL_ID, model.getId());
                ObjectNode editorJsonNode = (ObjectNode) objectMapper.readTree(new String(repositoryService.getModelEditorSource(model.getId()), "utf-8"));
                modelNode.set("model", editorJsonNode);

            }
            catch (Exception e)
            {
                LOGGER.error("创建模型json失败", e);
                throw  new ActivitiException("创建模型json失败，模型ID=" + modelId,e);
            }
        }
        return modelNode;
    }

    /**
     * 查询模型信息
     * 
     * @param modelId 模型ID
     * @return 模型信息
     */
    @Override
    public Model selectModelById(String modelId)
    {
        return repositoryService.getModel(modelId);
    }

    /**
     * 根据模型ID批量删除
     * 
     * @param ids 需要删除的数据ID
     * @return
     */
    @Override
    public boolean deleteModelIds(String ids)
    {
        boolean result = true;
        try
        {
            String[] modelIds = Convert.toStrArray(ids);
            for (String modelId : modelIds)
            {
                repositoryService.deleteModel(modelId);
            }
        }
        catch (Exception e)
        {
            result = false;
            throw  new ActivitiException("删除模型失败，模型ID=" + ids,e);
        }
        return result;
    }

    /**
     * 发布模型为流程定义
     * 
     * @param modelId 模型ID
     * @return
     * @throws Exception
     */
    @Override
    public AjaxResult deployProcess(String modelId)
    {
        try {
        // 获取模型
        Model modelData = repositoryService.getModel(modelId);
        byte[] bytes = repositoryService.getModelEditorSource(modelData.getId());

        if (bytes == null)
        {
            return AjaxResult.error("模型数据为空，请先设计流程并成功保存，再进行发布。");
        }

        JsonNode modelNode = new ObjectMapper().readTree(bytes);

        BpmnModel model = new BpmnJsonConverter().convertToBpmnModel(modelNode);
        if (model.getProcesses().size() == 0)
        {
            return AjaxResult.error("数据模型不符要求，请至少设计一条主线流程。");
        }
        byte[] bpmnBytes = new BpmnXMLConverter().convertToXML(model);

        // 发布流程
        String processName = modelData.getName() + ".bpmn20.xml";
        Deployment deployment = repositoryService.createDeployment().name(modelData.getName()).addString(processName, new String(bpmnBytes, "UTF-8")).deploy();
        modelData.setDeploymentId(deployment.getId());
        repositoryService.saveModel(modelData);
        }catch (IOException e){
            LOGGER.error("发布流程出现获取流程json数据失败");
            throw  new ActivitiException("发布流程出现获取流程json数据失败，模型ID=" + modelId,e);
        }
        return AjaxResult.success();
    }

    /**
     * 获取资源文件信息
     * 
     * @param modelId 模型ID
     * @return 资源文件信息
     */
    @Override
    public byte[] getModelEditorSource(String modelId)
    {
        return repositoryService.getModelEditorSource(modelId);
    }
}
