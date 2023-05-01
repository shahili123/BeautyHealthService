package uk.ac.tees.w9598552.beautyhealthservice.ChatModule.Notifications;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface APIService {

    @Headers({"Content-Type:application/json","Authorization:key=AAAAkGX86-A:APA91bHoDmKIxKcZwRmXfsHGCdYnZotkMGsFq_WG7leglmPjcZKuC2Uilz2K3AcaCa5QZL7s6___5Cqul_lyGpHAdLFZ-n4wbLm4ok1xQWXNzebGFwXfDVy28aztbZ1H6yxA1rUj6sjF "})
    @POST("fcm/send")
    Call<MyResponse> sendNotification(@Body Sender body);
}
