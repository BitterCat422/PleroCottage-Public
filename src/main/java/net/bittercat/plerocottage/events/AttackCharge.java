package net.bittercat.plerocottage.events;

import java.util.Collections;
import java.util.Map;
import java.util.WeakHashMap;

import net.minecraft.world.entity.player.Player;
import net.minecraftforge.event.entity.player.AttackEntityEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

/**
 * 玩家这一次攻击的蓄力值。原版在造成伤害、调用 hurtEnemy 之前就把蓄力清零了，
 * 所以要在攻击开始时记下来，后面的伤害事件和物品逻辑读这里。
 */
@Mod.EventBusSubscriber(modid = "plerocottage")
public final class AttackCharge {
    private record Charge(long gameTime, float scale) {
    }

    private static final Map<Player, Charge> CHARGES = Collections.synchronizedMap(new WeakHashMap<>());

    private AttackCharge() {
    }

    @SubscribeEvent(priority = EventPriority.HIGHEST, receiveCanceled = true)
    public static void onAttack(AttackEntityEvent event) {
        Player player = event.getEntity();
        CHARGES.put(player, new Charge(player.level().getGameTime(), player.getAttackStrengthScale(0.5F)));
    }

    /** 本刻内发起的攻击用记下的值，其余情况（例如不是近战攻击）退回当前值。 */
    public static float of(Player player) {
        Charge charge = CHARGES.get(player);
        return charge != null && charge.gameTime() == player.level().getGameTime()
            ? charge.scale() : player.getAttackStrengthScale(0.5F);
    }
}
