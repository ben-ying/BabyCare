from django.conf.urls import url
import views


urlpatterns = [
        url(r'^$', views.index, name='index'),
        url(r'^create$', views.create_user, name='create-user'),
        url(r'^login$', views.login, name='login'),
        ]
