package fzn.project.java.web.webasyncdemo.controller;

import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyEmitter;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

@Log
@Controller
public class AsyncController {

    @Autowired
    private AsyncHelper asyncHelper;

    @GetMapping(path = "/streaming")
    public ResponseBodyEmitter streaming(@RequestParam long eventNumber, @RequestParam long intervalSec) throws IOException, InterruptedException {
        log.info("Start get.");

        ResponseBodyEmitter emitter = new ResponseBodyEmitter();
        asyncHelper.streaming(emitter, eventNumber, intervalSec);

        log.info("End get.");
        return emitter;
    }

    @Log
    @Component
    public static class AsyncHelper {
        // ...
        @Async
        public void streaming(ResponseBodyEmitter emitter, long eventNumber, long intervalSec) throws IOException, InterruptedException {
            log.info("Start Async processing.");

            for (long i = 1; i <= eventNumber; i++) {
                TimeUnit.SECONDS.sleep(intervalSec);
                emitter.send("msg" + i + "\r\n", MediaType.TEXT_PLAIN);
            }

            emitter.complete();

            log.info("End Async processing.");
        }
        // ...
    }
}
