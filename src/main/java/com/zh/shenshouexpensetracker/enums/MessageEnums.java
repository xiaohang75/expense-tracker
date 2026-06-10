package com.zh.shenshouexpensetracker.enums;

public enum MessageEnums {

    CAPTCHA_CODE_ERROR(40001, "验证码错误"),
    USER_NOT_EXIST(40002, "用户不存在"),
    NEW_PASSWORD_EQUAL_OLD_ERROR(40004, "新密码不能与旧密码相同"),
    MAJOR_CODE_HAS_EXIST(40005, "专业编码已存在"),
    MAJOR_NOT_EXIST(40006, "专业不存在"),
    SUBJECT_CODE_HAS_EXIST(40007, "学科编码已存在"),
    SUBJECT_NOT_EXIST(40008, "学科不存在"),
    SUBJECT_HAS_COURSE(40009, "该学科下关联课程不能删除"),
    COURSE_NOT_EXIST(40010, "课程不存在"),
    COURSE_CODE_HAS_EXIST(40011, "课程编码已存在"),
    COURSE_HAS_COURSE_CLASS(40012, "该课程下关联课程班级不能删除"),
    COURSE_CLASS_NOT_EXIST(40013, "课程班级不存在"),
    STUDENTNO_NOT_NULL(40014, "学号不能为空"),
    STUDENT_HAS_EXIST(40015, "该学生学号已存在"),
    STUDENT_NOT_EXIST(40016, "该学生不存在"),
    STUDENTO_HAS_EXIST(40017, "该学号已存在"),
    ID_NOT_NULL(40018, "id不能为空"),
    CLASS_CODE_HAS_EXIST(40019, "班级编码已存在"),
    CLASS_NOT_EXIST(40020, "班级不存在"),
    TEACHER_CODE_NOT_NULL(40021, "教师编码不能为空"),
    TEACHER_NOT_EXIST(40022, "教师不存在"),
    USUAL_SCORE_NOT_NULL(40023, "平时成绩不能为空"),
    EXAM_SCORE_NOT_NULL(40024, "考试成绩不能为空"),
    USUAL_RATE_ERROR(40025, "平时成绩占比错误"),
    EXAM_RATE_ERROR(40026, "考试成绩占比错误"),
    TOTAL_RATE_NOT_100(40027, "总占比不能超过100"),
    SCORE_NOT_EXIST(40028, "该学生没有该课程的考试成绩"),
    USUAL_SCORE_OR_EXAM_SCORE_NOT_NULL(40029, "平时成绩或考试成绩不能为空"),
    USUAL_SCORE_NOT_BETWEEN_0_100(40030, "平时成绩或考试成绩必须在0-100之间"),
    EXAM_SCORE_NOT_BETWEEN_0_100(40031, "考试成绩必须在0-100之间"),
    STUDENT_NOT_ENROLLED_IN_COURSE_CLASS(40032, "该学生未选修该课程班级"),
    NOT_SELECT_STUDENT(40033, "请选择学生"),
    CLASS_CODE_NOT_NULL(40044, "班级编号不能为空"),
    SUBMIT_TOKEN_NOT_NULL(40034, "提交令牌不能为空"),
    STUDENT_ID_NOT_NULL(40035, "学生ID不能为空"),
    RECEIVE_EMAIL_NOT_NULL(40036, "接收邮箱不能为空"),
    STUDENT_WARNING_NOT_EXIST(40037, "该学生没有预警状态"),
    STUDENT_EMAIL_NOT_EXIST(40038, "该学生邮箱信息还未录入, 请先使用系统通知");


    private Integer code;
    private String message;

    MessageEnums(Integer code, String message) {
        this.code = code;
        this.message = message;
    }

    public Integer getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }
}
