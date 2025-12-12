package com.zredtea.TeaWIKI.controller;

import com.zredtea.TeaWIKI.DTO.Result;
import com.zredtea.TeaWIKI.DTO.request.Comment.CommentCommitDTO;
import com.zredtea.TeaWIKI.DTO.request.Comment.CommentUpdateDTO;
import com.zredtea.TeaWIKI.DTO.response.CommentDTO;
import com.zredtea.TeaWIKI.common.exception.BusinessException;
import com.zredtea.TeaWIKI.common.exception.ExceptionEnum;
import com.zredtea.TeaWIKI.costumer.annotation.CurrentUser;
import com.zredtea.TeaWIKI.service.CommentService;
import com.zredtea.TeaWIKI.service.CommentVoteService;
import com.zredtea.TeaWIKI.service.TeacherService;
import com.zredtea.TeaWIKI.service.UserService;
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

@Tag(name = "评论控制器", description = "用于管理评论接口")
@RestController
@RequestMapping("/comment")
public class CommentController {
    @Autowired
    private CommentService commentService;

    @Autowired
    private UserService userService;

    @Autowired
    private TeacherService teacherService;

    @Autowired
    private CommentVoteService commentVoteService;

    @Operation(summary = "提交新评论")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "提交成功")
    })
    @Parameters(value = {
            @Parameter(name = "userId", description = "用户ID", required = true, in = ParameterIn.COOKIE)
    })
    @PostMapping("/commit")
    public Result<CommentDTO> commitComment(@CurrentUser Integer userId,
                                            @RequestBody @Valid CommentCommitDTO dto) {
        if(dto == null) {
            throw new BusinessException(ExceptionEnum.INPUT_IS_NULL);
        }
        Integer teacherId = dto.getTeacherId();
        if(commentService.isCommentExist(userId, teacherId)) {
            throw new BusinessException(ExceptionEnum.COMMENT_HAS_EXIST);
        }
        if(!teacherService.isTeacherExist(teacherId)) {
            throw new BusinessException(ExceptionEnum.COMMENT_CONNECT_NOT_FOUND);
        }

        CommentDTO result = commentService.createComment(dto, userId);
        return Result.success(result);
    }

    @Operation(summary = "更新评论")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "更新成功")
    })
    @Parameters(value = {
            @Parameter(name = "userId", description = "用户ID", required = true, in = ParameterIn.COOKIE)
    })
    @PutMapping("/update")
    public Result<CommentDTO> updateComment(@CurrentUser Integer userId,
                                            @RequestBody @Valid CommentUpdateDTO dto) {
        if(dto == null) {
            throw new BusinessException(ExceptionEnum.INPUT_IS_NULL);
        }
        if(!commentService.isCommentExist(dto.getCommentId())) {
            throw new BusinessException(ExceptionEnum.COMMENT_NOT_FOUND);
        }
        if(!commentService.getById(dto.getCommentId()).getUserId().
                equals(userId)) {
            throw new BusinessException(ExceptionEnum.PERMISSION_ERROR);
        }
        CommentDTO result = commentService.updateComment(dto, userId);
        return Result.success(result);
    }

    @Operation(summary = "删除评论")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "删除成功")
    })
    @Parameters(value = {
            @Parameter(name = "userId", description = "用户ID", required = true, in = ParameterIn.COOKIE)
    })
    @DeleteMapping("/delete")
    public Result<Boolean> deleteComment(@CurrentUser Integer userId,
                                         @RequestParam Integer commentId) {
        if(!commentService.isCommentExist(commentId)) {
            throw new BusinessException(ExceptionEnum.COMMENT_NOT_FOUND);
        }
        if(!commentService.getById(commentId).getUserId()
                .equals(userId)) {
            throw new BusinessException(ExceptionEnum.PERMISSION_ERROR);
        }

        Boolean result = commentService.deleteComment(commentId, userId);
        return Result.success(result);
    }

    @Operation(summary = "获取教师所有评论")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "获取成功")
    })
    @Parameters(value = {
            @Parameter(name = "teacherId", description = "教师ID",  required = true, in = ParameterIn.PATH)
    })
    @GetMapping("/{teacherId}")
    public Result<List<CommentDTO>> getAllCommentsByTeacher(@PathVariable Integer teacherId) {
//                                                            @RequestParam String sortType) {
        if(teacherId == null) {
            throw new BusinessException(ExceptionEnum.INPUT_IS_NULL);
        }
        List<CommentDTO> result = commentService.searchCommentsByTeacherId(teacherId, null);
        return Result.success(result);
    }
}
