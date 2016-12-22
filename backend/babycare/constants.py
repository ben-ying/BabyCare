#!/usr/bin/env python
# -*- coding: utf-8 -*- 
from django.utils.translation import ugettext_lazy as _

MIN_PASSWORD_LEN = 6

# register
CODE_SUCCESS = 200
CODE_EMPTY_USER = 201
CODE_EMPTY_EMAIL = 202
CODE_EMPTY_PASSWORD = 203
CODE_INVALID_EMAIL = 204
CODE_INVALID_PASSWORD = 205
CODE_DUPLICATE_USER = 206
CODE_DUPLICATE_EMAIL = 207
CODE_DUPLICATE_PHONE = 208
CODE_NOT_EXISTS_EMAIL = 209

# register message
MSG_EMPTY_USER = _(u'用户名不能为空')
MSG_EMPTY_EMAIL = _(u'邮箱不能为空')
MSG_EMPTY_PASSWORD = _(u'密码不能为空')
MSG_INVALID_EMAIL = _(u'密码不能少于6位')
MSG_INVALID_PASSWORD = _(u'邮箱格式不正确')
MSG_DUPLICATE_USER = _(u'该用户已存在')
MSG_DUPLICATE_EMAIL = _(u'该邮箱已存在')
MSG_DUPLICATE_PHONE = _(u'该手机号码已存在')
MSG_NOT_EXISTS_EMAIL = _(u'该邮箱不存在')

