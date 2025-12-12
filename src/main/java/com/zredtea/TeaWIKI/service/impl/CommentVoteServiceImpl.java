package com.zredtea.TeaWIKI.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zredtea.TeaWIKI.DTO.request.CommentVote.CommentVoteCommitDTO;
import com.zredtea.TeaWIKI.DTO.response.CommentVoteDTO;
import com.zredtea.TeaWIKI.common.exception.BusinessException;
import com.zredtea.TeaWIKI.common.exception.ExceptionEnum;
import com.zredtea.TeaWIKI.entity.CommentVote;
import com.zredtea.TeaWIKI.mapper.CommentVoteMapper;
import com.zredtea.TeaWIKI.service.CommentVoteService;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class CommentVoteServiceImpl extends ServiceImpl<CommentVoteMapper, CommentVote>
                                    implements CommentVoteService {
    private CommentVoteMapper commentVoteMapper;

    @Override
    public boolean commitVote(Integer userId, CommentVoteCommitDTO dto) {
        CommentVote commentVote = convertToEntity(dto);
        commentVote.setUserId(userId);
        boolean success = save(commentVote);
        if(!success) {
            throw new BusinessException(ExceptionEnum.DATABASE_ERROR);
        }
        return true;
    }

    @Override
    public boolean cancelVote(Integer userId, Integer commentId) {
        CommentVoteMapper commentVoteMapper = getBaseMapper();
        CommentVote commentVote = commentVoteMapper.selectCommentVoteByUnionId(userId, commentId);
        if(commentVote == null) {
            throw new BusinessException(ExceptionEnum.CVOTE_NOT_FOUND);
        }
        boolean success = removeById(commentVote);
        if(!success) {
            throw new BusinessException(ExceptionEnum.DATABASE_ERROR);
        }
        return true;
    }

    @Override
    public boolean toggleVote(Integer userId, Integer commentId) {
        CommentVoteMapper commentVoteMapper = getBaseMapper();
        CommentVote commentVote = commentVoteMapper.selectCommentVoteByUnionId(userId, commentId);
        if(commentVote == null) {
            throw new BusinessException(ExceptionEnum.CVOTE_NOT_FOUND);
        }
        String nowType = commentVote.getVoteType();
        if(nowType.equals("like")) {
            commentVote.setVoteType("dislike");
        } else {
            commentVote.setVoteType("like");
        }
        boolean success = updateById(commentVote);
        if(!success) {
            throw new BusinessException(ExceptionEnum.DATABASE_ERROR);
        }
        return true;
    }

    @Override
    public Integer countLikes(Integer commentId) {
        return commentVoteMapper.countLikesByCommentId(commentId);
    }

    @Override
    public Integer countDislikes(Integer commentId) {
        return commentVoteMapper.countDislikesByCommentId(commentId);
    }

    CommentVote convertToEntity(CommentVoteCommitDTO dto) {
        CommentVote entity = new CommentVote();
        entity.setCommentId(dto.getCommentId());
        entity.setVoteType(dto.getVoteType());
        entity.setCreatedAt(LocalDateTime.now());
        entity.setUpdatedAt(LocalDateTime.now());
        return entity;
    }

    CommentVoteDTO convertToDTO(CommentVote entity) {
        CommentVoteDTO dto = new CommentVoteDTO();
        dto.setVoteId(entity.getVoteId());
        dto.setCommentId(entity.getCommentId());
        dto.setUserId(entity.getUserId());
        dto.setVoteType(entity.getVoteType());
        dto.setCreatedAt(entity.getCreatedAt());
        dto.setUpdatedAt(entity.getUpdatedAt());
        return dto;
    }
}
