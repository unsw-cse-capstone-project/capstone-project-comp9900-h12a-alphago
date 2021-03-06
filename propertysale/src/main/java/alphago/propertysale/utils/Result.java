package alphago.propertysale.utils;

import lombok.Data;

/**
 * @program: vueblog
 * @description: The Wrapper of the Result of all Controllers' return value
 **/
@Data
public class Result {
    private int code;
    private String msg;
    private Object result;

    public Result() {
    }

    public Result(int code, String msg, Object result) {
        this.code = code;
        this.msg = msg;
        this.result = result;
    }

    public static Result success(Object result){
        return new Result(200 , "success" , result);
    }

    public static Result fail(String failMsg){
        return new Result(400 , failMsg , null);
    }

    public static Result fail(int code , String failMsg){
        return new Result(code , failMsg , null);
    }
}