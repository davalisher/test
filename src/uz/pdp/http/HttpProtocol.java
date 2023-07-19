package uz.pdp.http;

import uz.pdp.http.Response;

public abstract class HttpProtocol {
    
    protected  Response sendRequest(Request request){
        return null;
    }
    protected  void sendResponse(Response response){};
}
