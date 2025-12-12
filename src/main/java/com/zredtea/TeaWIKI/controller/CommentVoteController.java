package com.zredtea.TeaWIKI.controller;

import com.zredtea.TeaWIKI.DTO.Result;
import com.zredtea.TeaWIKI.DTO.request.CommentVote.CommentVoteCommitDTO;
import com.zredtea.TeaWIKI.common.exception.BusinessException;
import com.zredtea.TeaWIKI.common.exception.ExceptionEnum;
import com.zredtea.TeaWIKI.costumer.annotation.CurrentUser;
import com.zredtea.TeaWIKI.service.CommentService;
import com.zredtea.TeaWIKI.service.CommentVoteService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Tag(name = "点赞点踩控制器")
@RestController
@RequestMapping("/commentVote/")
public class CommentVoteController {
    @Autowired
    private CommentVoteService commentVoteService;

    @Autowired
    private CommentService commentService;

    @Operation(summary = "点赞点踩")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "成功")
    })
    @Parameters(value = {
            @Parameter(name = "userId", description = "用户ID", required = true, in = ParameterIn.COOKIE)
    })
    @PostMapping("/vote")
    public Result<Boolean> vote(@CurrentUser Integer userId,
                                @RequestBody CommentVoteCommitDTO dto) {
        if(dto == null) {
            throw new BusinessException(ExceptionEnum.INPUT_IS_NULL);
        }
        if(!dto.getVoteType().equals("like") && !dto.getVoteType().equals("dislike")) {
            throw new BusinessException((ExceptionEnum.ILLEGAL_ARGUMENT));
        }
        boolean result = commentVoteService.commitVote(userId,dto);
        return Result.success(result);
    }

    @Operation(summary = "切换点赞点踩")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "成功")
    })
    @Parameters(value = {
            @Parameter(name = "userId", description = "用户ID", required = true, in = ParameterIn.COOKIE)
    })
    @PutMapping("/toggle")
    public Result<Boolean> toggle(@CurrentUser Integer userId,
                                  @RequestBody @Schema(name = "请求参数") Map<String, Integer> request) {
        Integer commentId = request.get("commentId");
        if(!commentService.isCommentExist(commentId)) {
            throw new BusinessException(ExceptionEnum.COMMENT_NOT_FOUND);
        }
        boolean result = commentVoteService.toggleVote(userId,commentId);
        return Result.success(result);
    }

    @Operation(summary = "取消点赞点踩")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "成功")
    })
    @Parameters(value = {
            @Parameter(name = "userId", description = "用户ID", required = true, in = ParameterIn.COOKIE)
    })
    @DeleteMapping("/cancel")
    public Result<Boolean> cancel(@CurrentUser Integer userId,
                                  @RequestBody @Schema(name = "请求参数") Map<String, Integer> request) {
        Integer commentId = request.get("commentId");
        if(!commentService.isCommentExist(commentId)) {
            throw new BusinessException(ExceptionEnum.COMMENT_NOT_FOUND);
        }
        boolean result = commentVoteService.cancelVote(userId,commentId);
        return Result.success(result);
    }
}

