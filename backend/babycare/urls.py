from django.conf.urls import url
from rest_framework import renderers
from rest_framework.urlpatterns import format_suffix_patterns
from views.user_views import UserViewSet, login_view, send_verify_code_view, reset_password_with_verify_code_view
from views.user_views import api_root

from babycare.views.views import send_feedback
from babycare.views.event_views import EventViewSet, like_view

user_list = UserViewSet.as_view({
    'get': 'list',
    'post': 'create'
})

user_detail = UserViewSet.as_view({
    'get': 'retrieve',
    'put': 'update',
    'patch': 'partial_update',
    'delete': 'destroy'
})

# user_login = LoginViewSet.as_view({
#     'post': 'retrieve',
# })

event_list = EventViewSet.as_view({
    'get': 'list',
    'post': 'create',
})

event_detail = EventViewSet.as_view({
    'get': 'retrieve',
    'put': 'update',
    'patch': 'partial_update',
    'delete': 'destroy'
})

user_highlight = UserViewSet.as_view({
    'get': 'highlight'
}, renderer_classes=[renderers.StaticHTMLRenderer])

urlpatterns = [
    url(r'^$', api_root, name='index'),
    url(r'^users/$', user_list, name='user-list'),
    url(r'^users/(?P<pk>[0-9]+)$', user_detail, name='user-detail'),
    url(r'^users/(?P<pk>[0-9]+)/highlight/$', user_highlight, name='user-highlight'),
    url(r'^user/login$', login_view, name='user-login'),
    url(r'^user/send_verify_code$', send_verify_code_view, name='send-verify-code'),
    url(r'^user/reset_password$', reset_password_with_verify_code_view, name='reset-password-with-verify-code'),
    url(r'^events/$', event_list, name='event-list'),
    url(r'^events/(?P<pk>[0-9]+)', event_detail, name='event-detail'),
    url(r'^event/like$', like_view, name='like'),
    url(r'^feedback/$', send_feedback, name='send-feedback'),
]

urlpatterns = format_suffix_patterns(urlpatterns)
