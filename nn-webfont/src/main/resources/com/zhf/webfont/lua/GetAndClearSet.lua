---
--- Generated by EmmyLua(https://github.com/EmmyLua)
--- Created by 10276.
--- DateTime: 2023/1/29 1:07
---
local list = redis.call('SMEMBERS',KEYS[1])
redis.call('DEL',KEYS[1])
--return cjson.encode(list)
return list