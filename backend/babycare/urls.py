from django.conf.urls import url
from rest_framework import renderers
from rest_framework.urlpatterns import format_suffix_patterns

from babycare.views.comment_views import CommentViewSet
from babycare.views.event_views import EventViewSet, like_view, multiply_events_view, delete_all_events_view
from babycare.views.views import send_feedback, about_us_view, RedEnvelopeViewSet, IaerViewSet
from views.user_views import UserViewSet, login_view, send_verify_code_view, reset_password_with_verify_code_view, \
    get_app_info
from views.user_views import api_root

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

red_envelope_list = RedEnvelopeViewSet.as_view({
    'get': 'list',
    'post': 'create'
})

red_envelope_detail = RedEnvelopeViewSet.as_view({
    'get': 'retrieve',
    'delete': 'destroy'
})

iaer_list = IaerViewSet.as_view({
    'get': 'list',
    'post': 'create'
})

iaer_detail = IaerViewSet.as_view({
    'get': 'retrieve',
    'delete': 'destroy'
})

# user_login = LoginViewSet.as_view({
#     'post': 'retrieve',
# })

event_list = EventViewSet.as_view({
    'get': 'list',
    'post': 'create',
})

comment_list = CommentViewSet.as_view({
    'get': 'list',
    'post': 'create',
})

comment_detail = CommentViewSet.as_view({
    'get': 'retrieve',
    'put': 'update',
    'patch': 'partial_update',
    'delete': 'destroy'
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
    url(r'^user/get_app_info$', get_app_info, name='get-app-info'),
    url(r'^events/$', event_list, name='event-list'),
    url(r'^events/(?P<pk>[0-9]+)', event_detail, name='event-detail'),
    url(r'^event/like$', like_view, name='like'),
    url(r'^event/comments/$', comment_list, name='comment-list'),
    url(r'^event/comments/(?P<pk>[0-9]+)', comment_detail, name='comment-detail'),
    url(r'^feedback/$', send_feedback, name='send-feedback'),
    url(r'^about_us/$', about_us_view, name='about-us'),
    url(r'^envelopes/$', red_envelope_list, name='red-envelope-list'),
    url(r'^envelopes/(?P<pk>[0-9]+)$', red_envelope_detail, name='red-envelope-detail'),
    url(r'^iaers/$', iaer_list, name='iaer-list'),
    url(r'^iaers/(?P<pk>[0-9]+)$', iaer_detail, name='iaer-detail'),

    # for test
    url(r'^event/multiply_x2$', multiply_events_view, name='send-feedback'),
    url(r'^event/delete_all$', delete_all_events_view, name='send-feedback'),
]

urlpatterns = format_suffix_patterns(urlpatterns)
