//自带player不需要传参
var a = function() {
    var level = "%player_health%";
    TextUtil.static.sendMessage(player, "&b你的&c血量为&f: &a" + level);
    return level >= 10;
}
a();