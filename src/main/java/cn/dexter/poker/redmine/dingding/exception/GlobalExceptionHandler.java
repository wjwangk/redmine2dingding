

package cn.dexter.poker.redmine.dingding.exception;

import cn.dexter.poker.redmine.dingding.domain.BizStatusEnum;
import cn.dexter.poker.redmine.dingding.domain.Result;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;


@ControllerAdvice
public class GlobalExceptionHandler {

    private final Logger LOGGER = LoggerFactory.getLogger("errorMessage");

    @ExceptionHandler(value = Throwable.class)
    @ResponseBody
    public Result<Throwable> jsonErrorHandler(HttpServletRequest req, Throwable e) throws Exception {
        LOGGER.error(e.getMessage(), e);
        return new Result<>(BizStatusEnum.SYS_ERR.getCode(), e.getMessage());
    }

}
