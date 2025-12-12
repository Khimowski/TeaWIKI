package com.zredtea.TeaWIKI.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zredtea.TeaWIKI.DTO.request.CourseTeacher.CourseTeacherCommitDTO;
import com.zredtea.TeaWIKI.DTO.request.CourseTeacher.CourseTeacherDeleteDTO;
import com.zredtea.TeaWIKI.common.exception.BusinessException;
import com.zredtea.TeaWIKI.common.exception.ExceptionEnum;
import com.zredtea.TeaWIKI.entity.Course;
import com.zredtea.TeaWIKI.entity.CourseTeacher;
import com.zredtea.TeaWIKI.entity.Teacher;
import com.zredtea.TeaWIKI.mapper.CourseMapper;
import com.zredtea.TeaWIKI.mapper.CourseTeacherMapper;
import com.zredtea.TeaWIKI.mapper.TeacherMapper;
import com.zredtea.TeaWIKI.service.CourseService;
import com.zredtea.TeaWIKI.service.CourseTeacherService;
import com.zredtea.TeaWIKI.service.TeacherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class CourseTeacherServiceImpl extends ServiceImpl<CourseTeacherMapper, CourseTeacher>
                                      implements CourseTeacherService {
    @Autowired
    private CourseTeacherMapper courseTeacherMapper;

    @Autowired
    private TeacherMapper teacherMapper;

    @Autowired
    private CourseMapper courseMapper;

    @Override
    public boolean commitCTConnect(CourseTeacherCommitDTO dto) {
        CourseTeacher courseTeacher = convertToEntity(dto);
        boolean success = save(courseTeacher);
        if(!success) {
            throw new BusinessException(ExceptionEnum.DATABASE_ERROR);
        }
        return true;
    }

    @Override
    public boolean cancelCTConnect(CourseTeacherDeleteDTO dto) {
        CourseTeacher courseTeacher = convertToEntity(dto);
        boolean success = removeById(courseTeacher);
        if(!success) {
            throw new BusinessException(ExceptionEnum.DATABASE_ERROR);
        }
        return true;
    }

    @Override
    public List<Teacher> getTeachersByCourseId(Integer courseId) {
        courseTeacherMapper = getBaseMapper();
        List<CourseTeacher> CTConnects = courseTeacherMapper.selectAllByCourseId(courseId);
        List<Teacher> teachers = new ArrayList<>();
        for(CourseTeacher entity : CTConnects) {
            Teacher teacher = teacherMapper.selectById(entity.getTeacherId());
            teachers.add(teacher);
        }
        return teachers;
    }

    @Override
    public List<Course> getCoursesByTeacherId(Integer teacherId) {
        List<CourseTeacher> CTConnects = courseTeacherMapper.selectAllByTeacherId(teacherId);
        List<Course> courses = new ArrayList<>();
        for(CourseTeacher entity : CTConnects) {
            Course course = courseMapper.selectById(entity.getCourseId());
            courses.add(course);
        }
        return courses;
    }

    @Override
    public boolean isCTConnectExist(Integer teacherId, Integer courseId) {
        return courseTeacherMapper.selectByUnionId(teacherId, courseId) != null;
    }

    CourseTeacher convertToEntity(CourseTeacherCommitDTO dto) {
        CourseTeacher entity = new CourseTeacher();
        entity.setTeacherId(dto.getTeacherId());
        entity.setCourseId(dto.getCourseId());
        return entity;
    }

    CourseTeacher convertToEntity(CourseTeacherDeleteDTO dto) {
        return courseTeacherMapper.selectByUnionId(dto.getTeacherId(), dto.getCourseId());
    }
}
