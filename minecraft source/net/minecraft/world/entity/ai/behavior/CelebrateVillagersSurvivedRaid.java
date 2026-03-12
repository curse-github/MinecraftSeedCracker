/*    */ package net.minecraft.world.entity.ai.behavior;
/*    */ 
/*    */ import com.google.common.collect.ImmutableMap;
/*    */ import it.unimi.dsi.fastutil.ints.IntList;
/*    */ import java.util.List;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.core.component.DataComponents;
/*    */ import net.minecraft.server.level.ServerLevel;
/*    */ import net.minecraft.util.RandomSource;
/*    */ import net.minecraft.util.Util;
/*    */ import net.minecraft.world.entity.LivingEntity;
/*    */ import net.minecraft.world.entity.npc.villager.Villager;
/*    */ import net.minecraft.world.entity.projectile.FireworkRocketEntity;
/*    */ import net.minecraft.world.entity.projectile.Projectile;
/*    */ import net.minecraft.world.entity.raid.Raid;
/*    */ import net.minecraft.world.item.DyeColor;
/*    */ import net.minecraft.world.item.ItemStack;
/*    */ import net.minecraft.world.item.Items;
/*    */ import net.minecraft.world.item.component.FireworkExplosion;
/*    */ import net.minecraft.world.item.component.Fireworks;
/*    */ 
/*    */ public class CelebrateVillagersSurvivedRaid
/*    */   extends Behavior<Villager>
/*    */ {
/*    */   private Raid currentRaid;
/*    */   
/* 27 */   public CelebrateVillagersSurvivedRaid(int minDuration, int maxDuration) { super(ImmutableMap.of(), minDuration, maxDuration); }
/*    */ 
/*    */ 
/*    */   
/*    */   protected boolean checkExtraStartConditions(ServerLevel level, Villager body) {
/* 32 */     BlockPos testPos = body.blockPosition();
/* 33 */     this.currentRaid = level.getRaidAt(testPos);
/* 34 */     return (this.currentRaid != null && this.currentRaid.isVictory() && MoveToSkySeeingSpot.hasNoBlocksAbove(level, body, testPos));
/*    */   }
/*    */ 
/*    */ 
/*    */   
/* 39 */   protected boolean canStillUse(ServerLevel level, Villager body, long timestamp) { return (this.currentRaid != null && !this.currentRaid.isStopped()); }
/*    */ 
/*    */ 
/*    */   
/*    */   protected void stop(ServerLevel level, Villager body, long timestamp) {
/* 44 */     this.currentRaid = null;
/* 45 */     body.getBrain().updateActivityFromSchedule(level.environmentAttributes(), level.getGameTime(), body.position());
/*    */   }
/*    */ 
/*    */   
/*    */   protected void tick(ServerLevel level, Villager body, long timestamp) {
/* 50 */     RandomSource random = body.getRandom();
/*    */     
/* 52 */     if (random.nextInt(100) == 0) {
/* 53 */       body.playCelebrateSound();
/*    */     }
/*    */     
/* 56 */     if (random.nextInt(200) == 0 && MoveToSkySeeingSpot.hasNoBlocksAbove(level, body, body.blockPosition())) {
/* 57 */       DyeColor color = (DyeColor)Util.getRandom(DyeColor.values(), random);
/* 58 */       int flightDuration = random.nextInt(3);
/* 59 */       ItemStack firework = getFirework(color, flightDuration);
/*    */       
/* 61 */       Projectile.spawnProjectile(new FireworkRocketEntity(body.level(), body, body.getX(), body.getEyeY(), body.getZ(), firework), level, firework);
/*    */     } 
/*    */   }
/*    */   
/*    */   private ItemStack getFirework(DyeColor color, int flightDuration) {
/* 66 */     ItemStack rocket = new ItemStack(Items.FIREWORK_ROCKET);
/* 67 */     rocket.set(DataComponents.FIREWORKS, new Fireworks((byte)flightDuration, 
/*    */           
/* 69 */           List.of(new FireworkExplosion(FireworkExplosion.Shape.BURST, 
/*    */               
/* 71 */               IntList.of(color.getFireworkColor()), 
/* 72 */               IntList.of(), false, false))));
/*    */ 
/*    */ 
/*    */ 
/*    */     
/* 77 */     return rocket;
/*    */   }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\entity\ai\behavior\CelebrateVillagersSurvivedRaid.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */