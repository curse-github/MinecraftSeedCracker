/*    */ package net.minecraft.tags;
/*    */ 
/*    */ import net.minecraft.core.registries.Registries;
/*    */ import net.minecraft.resources.Identifier;
/*    */ import net.minecraft.world.level.gameevent.GameEvent;
/*    */ 
/*    */ public class GameEventTags {
/*  8 */   public static final TagKey<GameEvent> VIBRATIONS = create("vibrations");
/*  9 */   public static final TagKey<GameEvent> WARDEN_CAN_LISTEN = create("warden_can_listen");
/* 10 */   public static final TagKey<GameEvent> SHRIEKER_CAN_LISTEN = create("shrieker_can_listen");
/* 11 */   public static final TagKey<GameEvent> IGNORE_VIBRATIONS_SNEAKING = create("ignore_vibrations_sneaking");
/* 12 */   public static final TagKey<GameEvent> ALLAY_CAN_LISTEN = create("allay_can_listen");
/*    */ 
/*    */   
/* 15 */   private static TagKey<GameEvent> create(String name) { return TagKey.create(Registries.GAME_EVENT, Identifier.withDefaultNamespace(name)); }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\tags\GameEventTags.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */