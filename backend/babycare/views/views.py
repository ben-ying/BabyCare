#!/usr/bin/env python
# -*- coding: utf-8 -*-

import time

from django.utils import timezone
from rest_framework.decorators import api_view

from babycare.constants import CODE_SUCCESS, FEEDBACK_FOOTER_IMAGE
from babycare.constants import MSG_SEND_FEEDBACK_SUCCESS
from babycare.models import Feedback, BabyUser
from babycare.utils import invalid_token_response, get_user_by_token, save_error_log, upload_image_to_oss
from babycare.utils import simple_json_response


@api_view(['POST'])
def send_feedback(request):
    description = request.data.get('description')
    base64_images = request.data.get('base64_images')
    token = request.data.get('token')
    user = get_user_by_token(token)

    try:
        if user:
            feedback = Feedback()
            feedback.baby = BabyUser.objects.get(user=user)
            feedback.description = description
            feedback.created = timezone.now()
            i = 0
            if base64_images:
                for image in base64_images:
                    i += 1
                    image_name = user.username + time.strftime('%Y%m%d%H%M%S') + FEEDBACK_FOOTER_IMAGE
                    image = upload_image_to_oss(image_name, image)
                    if i == 1:
                        feedback.image1 = image
                    if i == 2:
                        feedback.image2 = image
                    if i == 3:
                        feedback.image3 = image
                    if i == 4:
                        feedback.image4 = image
                    if i == 5:
                        feedback.image5 = image
                    if i == 6:
                        feedback.image6 = image
                    if i == 7:
                        feedback.image7 = image
                    if i == 8:
                        feedback.image8 = image
                    if i == 9:
                        feedback.image9 = image
            feedback.save()
            return simple_json_response(CODE_SUCCESS, MSG_SEND_FEEDBACK_SUCCESS)
        else:
            return invalid_token_response()
    except Exception as e:
        return save_error_log(request, e)
