from django.conf.urls import url
from rest_framework import renderers
from rest_framework.routers import DefaultRouter
from rest_framework.urlpatterns import format_suffix_patterns

from views import UserViewSet
from views import api_root

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

user_highlight = UserViewSet.as_view({
    'get': 'highlight'
}, renderer_classes=[renderers.StaticHTMLRenderer])


urlpatterns = [
        url(r'^$', api_root, name='index'),
        url(r'^users/$', user_list, name='user-list'),
        url(r'^users/(?P<pk>[0-9]+)$', user_detail, name='user-detail'),
        url(r'^users/(?P<pk>[0-9]+)/highlight/$', user_highlight, name='user-highlight'),
        ]

urlpatterns = format_suffix_patterns(urlpatterns)

