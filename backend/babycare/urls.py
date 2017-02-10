from django.conf.urls import url
import views


urlpatterns = [
        url(r'^$', views.index, name='index'),
        url(r'^register$', views.create_user, name='register-user'),
        url(r'^login$', views.login, name='login'),
        url(r'^addEvent$', views.add_event, name='add-event'),
        ]
