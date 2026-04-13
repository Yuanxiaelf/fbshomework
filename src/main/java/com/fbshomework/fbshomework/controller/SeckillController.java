package com.fbshomework.fbshomework.controller;

import com.fbshomework.fbshomework.service.SeckillService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/seckill")
@RequiredArgsConstructor
public class SeckillController {

    private final SeckillService seckillService;

    @GetMapping("/token")
    public String getToken(@RequestParam Long userId,
                           @RequestParam Long seckillId) {
        return seckillService.generateToken(userId, seckillId);
    }

    @PostMapping("/order")
    public String order(@RequestParam Long userId,
                        @RequestParam Long seckillId,
                        @RequestParam Long productId,
                        @RequestParam String token) {
        return seckillService.doSeckill(userId, seckillId, productId, token);
    }
}