package com.zredtea.TeaWIKI.controller;

import com.zredtea.TeaWIKI.DTO.Result;
import com.zredtea.TeaWIKI.DTO.request.Teacher.TeacherCreateDTO;
import com.zredtea.TeaWIKI.DTO.response.TeacherDTO;
import com.zredtea.TeaWIKI.service.TeacherService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@Tag(name = "教师控制器", description = "用于管理教师的接口")
@RestController
@RequestMapping("/teacher")
public class TeacherController {
    @Autowired
    private TeacherService teacherService;

    @Operation(summary = "创建教师")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "创建成功")
    })
    @PostMapping("/create")
    public Result<TeacherDTO> createTeacher(@RequestBody @Valid TeacherCreateDTO dto) {
        if(dto == null) {
            return Result.error(400,"dto不存在!");
        }
        TeacherDTO result = teacherService.createTeacher(dto);
        return Result.success(result);
    }

    @Operation(summary = "获取所有教师信息")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "获取成功")
    })
    @GetMapping("/teachers")
    public Result<List<TeacherDTO>> getAllTeachers() {
        List<TeacherDTO> result = teacherService.getAllTeachers();
        return Result.success(result);
    }

    @Operation(summary = "获取单个教师信息")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "获取成功")
    })
    @Parameters(value = {
            @Parameter(name = "teacherId", description = "教师ID", required = true, in = ParameterIn.PATH)
    })
    @GetMapping("/{teacherId}")
    public Result<TeacherDTO> getTeacher(@PathVariable Integer teacherId){
        if(teacherId == null) {
            return Result.error(400,"输入错误!");
        }
        if(!teacherService.isTeacherExist(teacherId)) {
            return Result.error(404,"该页面不存在!");
        }
        TeacherDTO result = teacherService.getTeacherById(teacherId);
        return Result.success(result);
    }
}
