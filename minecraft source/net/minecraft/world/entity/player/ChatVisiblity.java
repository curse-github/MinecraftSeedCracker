/*    */ package net.minecraft.world.entity.player;
/*    */ import com.mojang.serialization.Codec;
/*    */ import java.util.Objects;
/*    */ import java.util.function.IntFunction;
/*    */ import net.minecraft.network.chat.Component;
/*    */ import net.minecraft.util.ByIdMap;
/*    */ 
/*    */ public static enum ChatVisiblity {
/*    */   private static final IntFunction<ChatVisiblity> BY_ID;
/* 10 */   FULL(0, "options.chat.visibility.full"),
/* 11 */   SYSTEM(1, "options.chat.visibility.system"),
/* 12 */   HIDDEN(2, "options.chat.visibility.hidden"); public static final Codec<ChatVisiblity> LEGACY_CODEC;
/*    */   static  {
/* 14 */     BY_ID = ByIdMap.continuous(v -> v.id, values(), ByIdMap.OutOfBoundsStrategy.WRAP);
/* 15 */     Objects.requireNonNull(BY_ID); LEGACY_CODEC = Codec.INT.xmap(BY_ID::apply, v -> Integer.valueOf(v.id));
/*    */   }
/*    */   private final int id;
/*    */   private final Component caption;
/*    */   
/*    */   ChatVisiblity(int id, String key) {
/* 21 */     this.id = id;
/* 22 */     this.caption = Component.translatable(key);
/*    */   }
/*    */ 
/*    */   
/* 26 */   public Component caption() { return this.caption; }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\entity\player\ChatVisiblity.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */