#!/usr/bin/env python
# -*- coding: utf-8 -*-
import os

from django.utils.translation import ugettext_lazy as _

MIN_PASSWORD_LEN = 6
VERIFY_CODE_EXPIRED_TIME = 5 * 60 # 5 minutes
TEMP_IMAGE = os.path.join(os.path.dirname(__file__), 'temp.jpg')
TEMP_VIDEO = os.path.join(os.path.dirname(__file__), 'temp.mp4')
PROFILE_FOOTER_IMAGE = '_profile.jpg'
EVENT_FOOTER_IMAGE = '_event.jpg'
EVENT_FOOTER_VIDEO = '_event.mp4'
EVENT_FOOTER_VIDEO_THUMBNAIL = '_event_video_thumbnail.jpg'
FEEDBACK_FOOTER_IMAGE = '_feedback.jpg'
DIR_EVENT_IMAGE = '/image/'
DIR_EVENT_VIDEO = '/video/'
DIR_USER_PROFILE = '/profile/'
DIR_FEEDBACK = '/feedback/'

# register & user
CODE_SUCCESS = 200
CODE_EMPTY_USER = 1201
CODE_EMPTY_BABY_NAME = 1202
CODE_EMPTY_EMAIL = 1203
CODE_EMPTY_PASSWORD = 1204
CODE_INVALID_EMAIL = 1205
CODE_INVALID_PASSWORD = 1206
CODE_DUPLICATE_USER = 1207
CODE_DUPLICATE_EMAIL = 1208
CODE_DUPLICATE_PHONE = 1209
CODE_NOT_EXISTS_EMAIL = 1210
CODE_NOT_ACTIVE = 1211
CODE_INCORRECT_USER_NAME_OR_PASSWORD = 1212
CODE_EMPTY_VERIFY_CODE = 1213
CODE_INCORRECT_VERIFY_CODE = 1214
CODE_EXPIRED_VERIFY_CODE = 1215
CODE_USER_NOT_EXISTS = 1216

# event
CODE_EMPTY_EVENT = 1301
TYPE_IMAGE = 0
TYPE_VIDEO = 1
# comment
CODE_EMPTY_COMMENT = 1401

DATE_TIME_FORMAT = ('%Y-%m-%d %H:%M:%S')

CODE_NO_CONTENT = 204
CODE_INVALID_REQUEST = 400
CODE_INVALID_TOKEN = 401
CODE_EXCEPTION = 402
CODE_DUPLICATE = 403
MSG_204 = _(u'请求数据不存在')
MSG_400 = _(u'请求数据格式不正确')
MSG_401 = _(u'AccessToken异常')
MSG_402 = _(u'接口发生异常')
MSG_403 = _(u'重复保存')

# register message
MSG_EMPTY_USERNAME = _(u'用户名不能为空')
MSG_EMPTY_BABY_NAME = _(u'宝宝名不能为空')
MSG_EMPTY_EMAIL = _(u'邮箱不能为空')
MSG_EMPTY_PASSWORD = _(u'密码不能为空')
MSG_INVALID_EMAIL = _(u'邮箱格式不正确')
MSG_NO_SUCH_EMAIL = _(u'此邮箱不存在')
MSG_INVALID_PASSWORD = _(u'密码不能少于6位')
MSG_DUPLICATE_USER = _(u'该用户已被使用')
MSG_DUPLICATE_EMAIL = _(u'该邮箱已被使用')
MSG_DUPLICATE_PHONE = _(u'该手机号码已被使用')
MSG_NOT_EXISTS_EMAIL = _(u'该邮箱不存在')
MSG_CREATE_USER_SUCCESS = _(u'用户创建成功')
MSG_GET_USERS_SUCCESS = _(u'获取用户成功')
MSG_LOGIN_SUCCESS = _(u'登入成功')
MSG_UPDATE_USER_INFO_SUCCESS = _(u'更新用户信息成功')
MSG_NOT_ACTIVE_USER = _(u'该用户暂时不可用')
MSG_INCORRECT_USER_NAME_OR_PASSWORD = _(u'用户名或账号错误')
MSG_SEND_VERIFY_CODE_SUCCESS = _(u'验证码发送成功')
MSG_EMPTY_VERIFY_CODE = _(u'验证码不能为空')
MSG_INCORRECT_VERIFY_CODE = _(u'验证码错误')
MSG_EXPIRED_VERIFY_CODE = _(u'验证码已过期')
MSG_USER_NOT_EXISTS = _(u'该用户不存在')
MSG_GET_EVENTS_SUCCESS = _(u'获取动态成功')
MSG_GET_COMMENTS_SUCCESS = _(u'获取评论成功')
MSG_SEND_FEEDBACK_SUCCESS = _(u'反馈消息发送成功')
MSG_GET_USER_DETAIL_SUCCESS = _(u'获取用户信息成功')
MSG_ADD_LIKE_SUCCESS = _(u'点赞成功')

PASSWORD_VERIFY_CODE_EMAIL_SUBJECT = _(u'忘记密码验证码－嘟嘟手记')
PASSWORD_VERIFY_CODE_EMAIL_CONTENT = _(u'您的验证码：%s。\n本邮件是系统自动发送的，请勿直接回复！感谢您的访问，祝您使用愉快！')
MSG_GET_APP_INFO_SUCCESS = _(u'获取应用版本信息')

# event message
MSG_EMPTY_EVENT = _(u'数据不能全为空')
MSG_POST_EVENT_SUCCESS = _(u'发布成功')
MSG_DELETE_EVENT_SUCCESS = _(u'删除动态成功')
MSG_DELETE_COMMENT_SUCCESS = _(u'删除评论成功')
# comment
MSG_EMPTY_COMMENT_FIELD = _(u'数据格式不正确')
MSG_POST_COMMENT_SUCCESS = _(u'评论发布成功')
# about us
MSG_NO_CONTENT = _(u'<h1 align="center">暂无内容</h1>')
# red envelopes
MSG_GET_RED_ENVELOPES_SUCCESS = _(u'获取红包列表成功')
MSG_DELETE_RED_ENVELOPE_SUCCESS = _(u'删除红包成功')
MSG_ADD_RED_ENVELOPE_SUCCESS = _(u'添加红包成功')
# iaer
MSG_GET_IAERS_SUCCESS = _(u'获取收支列表成功')
MSG_DELETE_IAER_SUCCESS = _(u'删除收支成功')
MSG_ADD_IAER_SUCCESS = _(u'添加收支成功')

