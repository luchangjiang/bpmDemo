package com.river.bpmDemo.controller;

import com.river.bpmDemo.constant.StudentConstants;
import com.river.bpmDemo.model.dto.AuditingDto;
import com.river.bpmDemo.model.dto.ProcessAuditDTO;
import com.river.bpmDemo.model.dto.StudentApplyDto;
import com.river.bpmDemo.model.dto.uploadExtraDTO;
import com.river.bpmDemo.model.vo.ProjectInfoVO;
import com.river.bpmDemo.model.vo.TaskInfoVO;
import com.river.bpmDemo.util.R;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lombok.var;
import org.camunda.bpm.engine.RepositoryService;
import org.camunda.bpm.engine.RuntimeService;
import org.camunda.bpm.engine.TaskService;
import org.camunda.bpm.engine.runtime.ProcessInstance;
import org.camunda.bpm.engine.task.Task;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @program: bpmDemo
 * @description:
 * @author: River
 * @create: 2019-12-08 14:41
 **/
@Api
@RestController
@RequestMapping("/student")
@Slf4j
@AllArgsConstructor
public class StudentApplyController {
    private final String artifactId = "bpmDemoApplication";

    private final TaskService taskService;
    private final RuntimeService runtimeService;
    private final RepositoryService repositoryService;

    @ApiOperation("获取项目列表")
    @GetMapping("/getProjects")
    public R<List<ProjectInfoVO>> getProjects(){
        List<ProjectInfoVO> list = new ArrayList<>();
        /*String deploymentId = repositoryService.createDeploymentQuery()
                .deploymentName(artifactId).singleResult().getId();*/

        repositoryService.createProcessDefinitionQuery()
                .list()
                .forEach(pd->{
                    ProjectInfoVO info = new ProjectInfoVO();
                    info.setProjectId(pd.getId());
                    info.setProjectName(pd.getName());
                    info.setResourceName(pd.getDiagramResourceName());
                    info.setAppName(artifactId);
                    list.add(info);
                });
        return R.ok(list);
    }

    @ApiOperation("启动项目")
    @PostMapping("/startApply")
    public R<Boolean> startApply(@RequestBody StudentApplyDto dto){
        Map<String, Object> vars = new HashMap<>();
        vars.put(StudentConstants.SCHOOL, dto.getSchoolName());
        vars.put(StudentConstants.STUDENT, dto.getAssignee());
        vars.put(StudentConstants.ASSIGNEE_AUDITOR1, dto.getAssignee());

        ProcessInstance pi = runtimeService.startProcessInstanceById(dto.getProjectId(), vars);
        if(pi == null){
            return R.failed();
        }else {
            return R.ok();
        }
    }

    @ApiOperation("获取待审任务")
    @PostMapping("/getAuditing")
    public R<List<TaskInfoVO>> getAuditing(@RequestBody AuditingDto dto){
        List<TaskInfoVO> list = new ArrayList<>();

        List<Task> list1 = taskService.createTaskQuery()
                .processDefinitionId(dto.getProjectId())
                .taskAssignee(dto.getAssignee())
                .list();
        list1.forEach(ts->{
            TaskInfoVO vo = new TaskInfoVO();
            vo.setTaskId(ts.getId());
            vo.setTaskName(ts.getName());
            vo.setProjectId(ts.getProcessDefinitionId());
            vo.setAssignee(ts.getAssignee());
            vo.setCreateTime(ts.getCreateTime());
            list.add(vo);
        });
        return R.ok(list);
    }

    @ApiOperation("老师审核")
    @PostMapping("/processAudit1")
    public R processAudit1(@RequestBody ProcessAuditDTO dto){
        Task task = taskService.createTaskQuery()
                .taskId(dto.getTaskId())
                .singleResult();

        if(task !=null) {
            var vars = new HashMap<String, Object>();
            Object extraObj = runtimeService.getVariable(task.getProcessDefinitionId(), StudentConstants.EXTRA_INFO_1);
            if(extraObj != null) {
                vars.put(StudentConstants.EXTRA_INFO_1, false);
            }
            else{
                vars.put(StudentConstants.EXTRA_INFO_1, true);
            }
            vars.put(StudentConstants.APPROVED_1, dto.getApproved());
            vars.put(StudentConstants.STUDENT, dto.getAssignee());
            vars.put(StudentConstants.ASSIGNEE_UPLOADOR1, dto.getAssignee());
            taskService.complete(task.getId(), vars);
            return R.ok();
        }else{
            return R.failed();
        }
    }

    @ApiOperation("老师审核")
    @PostMapping("/processAudit2")
    public R processAudit2(@RequestBody ProcessAuditDTO dto){
        Task task = taskService.createTaskQuery()
                .taskId(dto.getTaskId())
                .singleResult();

        if(task !=null) {
            var vars = new HashMap<String, Object>();
            Object extraObj = runtimeService.getVariable(task.getProcessDefinitionId(), StudentConstants.EXTRA_INFO_1);
            if(extraObj != null) {
                vars.put(StudentConstants.EXTRA_INFO_2, false);
            }
            else{
                vars.put(StudentConstants.EXTRA_INFO_2, true);
            }
            vars.put(StudentConstants.APPROVED_2, dto.getApproved());
            vars.put(StudentConstants.ASSIGNEE_UPLOADOR2, dto.getAssignee());
            taskService.complete(task.getId(), vars);
            return R.ok();
        }else{
            return R.failed();
        }
    }

    @ApiOperation("补充材料")
    @PostMapping("/uploadExtra")
    public R uploadExtra(@RequestBody uploadExtraDTO dto){
        Task task = taskService.createTaskQuery().
                taskId(dto.getTaskId()).
                singleResult();
        if (task == null) {
            log.error("The task not found.");
            log.error("the assignee is {}, taskId is {}.", dto.getAssignee(), dto.getTaskId());
            return R.failed();
        }else {
            var vars = new HashMap<String, Object>();

            if("老师审核".equals(task.getName())) {
                vars.put(StudentConstants.ASSIGNEE_AUDITOR1, dto.getAssignee());
                vars.put(StudentConstants.EXTRA_INFO_1, dto.getExtraInfo());
            }else{
                vars.put(StudentConstants.ASSIGNEE_AUDITOR2, dto.getAssignee());
                vars.put(StudentConstants.EXTRA_INFO_2, dto.getExtraInfo());
            }
            taskService.complete(task.getId(), vars);
            return R.ok();
        }
    }

}
