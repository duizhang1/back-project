local value = redis.call('get',KEYS[1])
--判断redis中是否有该点赞记录
if value == nil then
    return false;
else
    --存在的话就直接将记录后缀改为0
    local tmp = string.sub(value,1,#value-1);
    local zero = 0;
    local newValue = tmp..zero
    redis.call('set',KEYS[1],newValue)
    return true;
end