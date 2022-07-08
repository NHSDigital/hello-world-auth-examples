public class App {

    public static void main(String[] args) throws Exception {

        String accessToken = Auth.getAccessToken();
        System.out.println(accessToken);

        String response = HelloWorld.makeApplicationRestrictedRequest(accessToken);
        System.out.println(response);
    }
}
