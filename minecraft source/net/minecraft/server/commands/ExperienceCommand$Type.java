/*     */ package net.minecraft.server.commands;
/*     */ 
/*     */ import java.util.function.BiConsumer;
/*     */ import java.util.function.BiPredicate;
/*     */ import java.util.function.ToIntFunction;
/*     */ import net.minecraft.server.level.ServerPlayer;
/*     */ import net.minecraft.util.Mth;
/*     */ import net.minecraft.world.entity.player.Player;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ static enum Type
/*     */ {
/* 137 */   POINTS("points", Player::giveExperiencePoints, (p, a) -> {
/* 138 */       if (a.intValue() >= p.getXpNeededForNextLevel()) {
/* 139 */         return false;
/*     */       }
/* 141 */       p.setExperiencePoints(a.intValue());
/* 142 */       return true;
/* 143 */     }p -> Mth.floor(p.experienceProgress * p.getXpNeededForNextLevel())),
/* 144 */   LEVELS("levels", ServerPlayer::giveExperienceLevels, (p, a) -> {
/* 145 */       p.setExperienceLevels(a.intValue());
/* 146 */       return true;
/* 147 */     }p -> p.experienceLevel);
/*     */   
/*     */   public final BiConsumer<ServerPlayer, Integer> add;
/*     */   public final BiPredicate<ServerPlayer, Integer> set;
/*     */   public final String name;
/*     */   private final ToIntFunction<ServerPlayer> query;
/*     */   
/*     */   Type(String name, BiConsumer<ServerPlayer, Integer> add, BiPredicate<ServerPlayer, Integer> set, ToIntFunction<ServerPlayer> query) {
/* 155 */     this.add = add;
/* 156 */     this.name = name;
/* 157 */     this.set = set;
/* 158 */     this.query = query;
/*     */   }
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\server\commands\ExperienceCommand$Type.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */