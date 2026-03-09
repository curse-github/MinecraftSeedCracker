/*    */ package net.minecraft.world.entity.animal.parrot;
/*    */ 
/*    */ import com.mojang.logging.LogUtils;
/*    */ import net.minecraft.server.level.ServerPlayer;
/*    */ import net.minecraft.util.ProblemReporter;
/*    */ import net.minecraft.world.entity.EntityType;
/*    */ import net.minecraft.world.entity.TamableAnimal;
/*    */ import net.minecraft.world.level.Level;
/*    */ import net.minecraft.world.level.storage.TagValueOutput;
/*    */ import org.slf4j.Logger;
/*    */ 
/*    */ public abstract class ShoulderRidingEntity
/*    */   extends TamableAnimal
/*    */ {
/* 15 */   private static final Logger LOGGER = LogUtils.getLogger();
/*    */   
/*    */   private static final int RIDE_COOLDOWN = 100;
/*    */   private int rideCooldownCounter;
/*    */   
/* 20 */   protected ShoulderRidingEntity(EntityType<? extends ShoulderRidingEntity> type, Level level) { super(type, level); }
/*    */ 
/*    */   
/*    */   public boolean setEntityOnShoulder(ServerPlayer player) {
/* 24 */     ProblemReporter.ScopedCollector reporter = new ProblemReporter.ScopedCollector(problemPath(), LOGGER); 
/* 25 */     try { TagValueOutput output = TagValueOutput.createWithContext(reporter, registryAccess());
/* 26 */       saveWithoutId(output);
/* 27 */       output.putString("id", getEncodeId());
/*    */       
/* 29 */       if (player.setEntityOnShoulder(output.buildResult()))
/* 30 */       { discard();
/* 31 */         boolean bool = true;
/*    */         
/* 33 */         reporter.close(); return bool; }  reporter.close(); } catch (Throwable throwable) { try { reporter.close(); } catch (Throwable throwable1) { throwable.addSuppressed(throwable1); }
/*    */        throw throwable; }
/* 35 */      return false;
/*    */   }
/*    */ 
/*    */   
/*    */   public void tick() {
/* 40 */     this.rideCooldownCounter++;
/* 41 */     super.tick();
/*    */   }
/*    */ 
/*    */   
/* 45 */   public boolean canSitOnShoulder() { return (this.rideCooldownCounter > 100); }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\entity\animal\parrot\ShoulderRidingEntity.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */