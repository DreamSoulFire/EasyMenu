var a = function(player) {
    var level = "%player_health%";
    TextUtil.static.sendMessage(player, "&b你的等级为&f: &a" + level);
    return level >= 10;
}
a();