package com.zredtea.TeaWIKI.controller;

import com.zredtea.TeaWIKI.DTO.Result;
import com.zredtea.TeaWIKI.DTO.request.Course.CourseCreateDTO;
import com.zredtea.TeaWIKI.DTO.request.Course.CourseDeleteDTO;
import com.zredtea.TeaWIKI.DTO.request.Course.CourseUpdateDTO;
import com.zredtea.TeaWIKI.DTO.response.CourseDTO;
import com.zredtea.TeaWIKI.common.exception.BusinessException;
import com.zredtea.TeaWIKI.common.exception.ExceptionEnum;
import com.zredtea.TeaWIKI.service.CourseService;
import com.zredtea.TeaWIKI.service.CourseTeacherService;
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

import java.util.List;

@Tag(name = "课程控制器", description = "用于管理课程接口")
@RestController
@RequestMapping("/course")
public class CourseController {
    @Autowired
    private CourseService courseService;

    @Autowired
    private TeacherService teacherService;

    @Autowired
    private CourseTeacherService courseTeacherService;

    @Operation(summary = "创建课程")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "创建成功")
    })
    @PostMapping("/create")
    public Result<CourseDTO> createCourse(@RequestBody @Valid CourseCreateDTO dto) {
        if (dto == null) {
            throw new BusinessException(ExceptionEnum.INPUT_IS_NULL);
        }
        CourseDTO result = courseService.createCourse(dto);
        return Result.success(result);
    }

    @Operation(summary = "更新课程")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "更新成功")
    })
    @PutMapping("/update")
    public Result<CourseDTO> updateCourse(@RequestBody @Valid CourseUpdateDTO dto) {
        if (dto == null) {
            throw new BusinessException(ExceptionEnum.INPUT_IS_NULL);
        }
        CourseDTO result = courseService.updateCourse(dto);
        return Result.success(result);
    }

    @Operation(summary = "删除课程")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "删除成功")
    })
    @DeleteMapping("/delete")
    public Result<Boolean> deleteCourse(@RequestBody @Valid CourseDeleteDTO dto) {
        if(dto == null) {
            throw new BusinessException(ExceptionEnum.INPUT_IS_NULL);
        }
        boolean result = courseService.deleteCourse(dto);
        return Result.success(result);
    }

    @Operation(summary = "查询课程")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "查询成功")
    })
    @Parameters(value = {
            @Parameter(name = "courseId", description = "课程ID", required = true, in = ParameterIn.PATH)
    })
    @GetMapping("/{courseId}")
    public Result<CourseDTO> getCourseById(@PathVariable Integer courseId) {
        if(courseId == null) {
            throw new BusinessException(ExceptionEnum.INPUT_IS_NULL);
        }
        CourseDTO result = courseService.getCourseById(courseId);
        return Result.success(result);
    }

    @Operation(summary = "查询所有课程")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "查询成功")
    })
    @GetMapping("/courses")
    public Result<List<CourseDTO>> getAllCourses() {
        List<CourseDTO> result = courseService.getAllCourses();
        return Result.success(result);
    }
}
