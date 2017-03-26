from django.conf.urls import include, url
from django.contrib import admin
from rest_framework.routers import DefaultRouter
from rest_framework.schemas import get_schema_view

from babycare.user_views import UserViewSet

router = DefaultRouter()
router.register(r'users', UserViewSet)

schema_view = get_schema_view(title='Pastebin API')

urlpatterns = [
    url(r'^', include(router.urls)),
    url(r'^babycare/', include('babycare.urls')),
    url(r'^admin/', admin.site.urls),
    url(r'^api-auth/', include('rest_framework.urls', namespace='rest_framework')),
    # api schema
    url('^schema/$', schema_view),
]
