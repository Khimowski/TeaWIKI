package com.zredtea.TeaWIKI.controller;


import com.zredtea.TeaWIKI.DTO.Result;
import com.zredtea.TeaWIKI.DTO.request.CourseTeacher.CourseTeacherCommitDTO;
import com.zredtea.TeaWIKI.DTO.request.CourseTeacher.CourseTeacherDeleteDTO;
import com.zredtea.TeaWIKI.DTO.response.CourseDTO;
import com.zredtea.TeaWIKI.DTO.response.TeacherDTO;
import com.zredtea.TeaWIKI.common.exception.BusinessException;
import com.zredtea.TeaWIKI.common.exception.ExceptionEnum;
import com.zredtea.TeaWIKI.entity.Teacher;
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
import java.util.Map;

@Tag(name = "课程教师连接控制器")
@RestController
@RequestMapping("/CTConnect")
public class CourseTeacherController {
    @Autowired
    private CourseTeacherService courseTeacherService;
    @Autowired
    private CourseService courseService;
    @Autowired
    private TeacherService teacherService;

    @Operation(summary = "创建课程教师连接")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "创建成功")
    })
    @PostMapping("/create")
    public Result<Boolean> createConnect(@RequestBody @Valid CourseTeacherCommitDTO dto) {
        if(dto == null) {
            throw new BusinessException(ExceptionEnum.INPUT_IS_NULL);
        }
        Boolean result = courseTeacherService.commitCTConnect(dto);
        return Result.success(result);
    }

    @Operation(summary = "删除课程教师连接")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "删除成功")
    })
    @DeleteMapping("/delete")
    public Result<Boolean> deleteConnect(@RequestBody @Valid CourseTeacherDeleteDTO dto) {
        if(dto == null) {
            throw new BusinessException(ExceptionEnum.INPUT_IS_NULL);
        }
        Boolean result = courseTeacherService.cancelCTConnect(dto);
        return Result.success(result);
    }

    @Operation(summary = "根据课程查询教师")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "查询")
    })
    @Parameters(value = {
            @Parameter(name = "courseId", description = "课程ID", required = true, in = ParameterIn.QUERY)
    })
    @GetMapping("/teachers")
    public Result<List<TeacherDTO>> getTeachersByCourseId(@RequestParam Integer courseId) {
        if(courseId == null) {
            throw new BusinessException(ExceptionEnum.INPUT_IS_NULL);
        }
        List<TeacherDTO> result = teacherService.getTeachersByCourseId(courseId);
        return Result.success(result);
    }

    @Operation(summary = "根据教师查询课程")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "查询")
    })
    @Parameters(value = {
            @Parameter(name = "teacherId", description = "教师ID", required = true, in = ParameterIn.QUERY)
    })
    @GetMapping("/courses")
    public Result<List<CourseDTO>> getCoursesByTeacherId(@RequestParam Integer teacherId) {
        if(teacherId == null) {
            throw new BusinessException(ExceptionEnum.INPUT_IS_NULL);
        }
        List<CourseDTO> result = courseService.getCourseByTeacherId(teacherId);
        return Result.success(result);
    }
}
