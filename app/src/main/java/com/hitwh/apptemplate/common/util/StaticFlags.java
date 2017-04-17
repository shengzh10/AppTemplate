package com.hitwh.apptemplate.common.util;

/**
 * @author SQY
 * @since 2017/3/14.
 */

public interface StaticFlags {
    /**
     * 与后台交互的状态
     */
    String STATUS_SUCCESS = "success"; // 成功
    String STATUS_FAIL = "fail"; // 失败
    /**
     * 安全检查
     */
    int TASK_DETAIL_QUESTCODE = 0x00; // 有返回跳转到执行巡检界面
    String CHECKING_TASK = "checking_task"; // 正在检查的任务
    String CHECKING_TASK_LIST_INDEX = "checking_task_list_index"; // 正在检查的任务编号
    String CHECKING_TASK_KIND = "checking_task_kind"; // 正在检查的任务列表
    /**
     * 任务类型
     */
    String TASK_KIND_DAY = "1"; // 任务类型-每天
    String TASK_KIND_WEEK = "7"; // 任务类型-每周
    String TASK_KIND_MONTH = "30"; // 任务类型-每月
    /**
     * 检查项目*
     */
    String TASK_ITEM_NAME_PREFIX = "检查项目 "; // 巡检任务标题前缀
    String UPLOAD_TASK_STATE = "upload_task_state"; // 上传任务状态
    String UPLOAD_PHOTO_STATE = "upload_photo_state"; // 上传照片状态
    String EXEC_PATROL_NFC_POINTCODE = "exec_patrol_nfc"; // 巡检任务标题前缀

    String EXEC_PATROL_TASKCODE = "exec_patrol_taskcode"; // 传递 taskCode
    int EXEC_PATROL_NFC = 0x01; // 有返回跳转到执行nfc扫描
    int EXEC_PATROL_CAMERA = 0x02; // 有返回跳转到执行 拍照
    int EXEC_PATROL_ITEM_CHECK = 0x03; // 有返回跳转到执行 条目检查
    String EXEC_PATROL_NFC_STATE = "exec_patrol_nfc_state"; // 执行nfc扫描的状态
    String EXEC_PATROL_CAMERA_STATE = "exec_patrol_camera_state"; // 执行拍照的状态
    String EXEC_PATROL_ITEM_CHECK_STATE = "exec_patrol_item_check_state"; // 执行条目检查的状态
    String EXEC_PATROL_ITEM_PROB_DESC = "exec_patrol_item_prob_desc"; // 执行条目检查 其他问题描述
    // *巡检任务成功标志位*
    String NFC_SUCCESS = "nfc_success"; // nfc 成功
    String CAMERA_SUCCESS = "camera_success"; // camera 成功
    String ITEM_SUCCESS = "item_success"; // item 成功
    /**
     * 扫描
     */
    int VAlIDSCANCOUNTS = 3; // 确认项点未失效的最大扫描次数
    /**
     * 拍照
     */
    int REQUEST_FROM_CAMERA = 0x04;
    int MAX_IMAGE_NUM = 12; // 执行巡检 拍照 最大图片数量
}
