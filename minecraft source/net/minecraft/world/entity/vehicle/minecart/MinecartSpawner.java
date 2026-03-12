/*    */ package net.minecraft.world.entity.vehicle.minecart;
/*    */ 
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.server.level.ServerLevel;
/*    */ import net.minecraft.world.entity.EntityType;
/*    */ import net.minecraft.world.item.Item;
/*    */ import net.minecraft.world.item.ItemStack;
/*    */ import net.minecraft.world.item.Items;
/*    */ import net.minecraft.world.level.BaseSpawner;
/*    */ import net.minecraft.world.level.Level;
/*    */ import net.minecraft.world.level.block.Blocks;
/*    */ import net.minecraft.world.level.block.state.BlockState;
/*    */ import net.minecraft.world.level.storage.ValueInput;
/*    */ import net.minecraft.world.level.storage.ValueOutput;
/*    */ 
/*    */ public class MinecartSpawner extends AbstractMinecart {
/* 17 */   private final BaseSpawner spawner = new BaseSpawner()
/*    */     {
/*    */       public void broadcastEvent(Level level, BlockPos pos, int id) {
/* 20 */         level.broadcastEntityEvent(MinecartSpawner.this, (byte)id);
/*    */       }
/*    */     };
/*    */   
/*    */   private final Runnable ticker;
/*    */   
/*    */   public MinecartSpawner(EntityType<? extends MinecartSpawner> type, Level level) {
/* 27 */     super(type, level);
/* 28 */     this.ticker = createTicker(level);
/*    */   }
/*    */ 
/*    */ 
/*    */   
/* 33 */   protected Item getDropItem() { return Items.MINECART; }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 38 */   public ItemStack getPickResult() { return new ItemStack(Items.MINECART); }
/*    */ 
/*    */   
/*    */   private Runnable createTicker(Level level) {
/* 42 */     return (level instanceof ServerLevel) ? (() -> 
/* 43 */       this.spawner.serverTick((ServerLevel)level, blockPosition())) : (() -> 
/* 44 */       this.spawner.clientTick(level, blockPosition()));
/*    */   }
/*    */ 
/*    */ 
/*    */   
/* 49 */   public BlockState getDefaultDisplayBlockState() { return Blocks.SPAWNER.defaultBlockState(); }
/*    */ 
/*    */ 
/*    */   
/*    */   protected void readAdditionalSaveData(ValueInput input) {
/* 54 */     super.readAdditionalSaveData(input);
/* 55 */     this.spawner.load(level(), blockPosition(), input);
/*    */   }
/*    */ 
/*    */   
/*    */   protected void addAdditionalSaveData(ValueOutput output) {
/* 60 */     super.addAdditionalSaveData(output);
/* 61 */     this.spawner.save(output);
/*    */   }
/*    */ 
/*    */ 
/*    */   
/* 66 */   public void handleEntityEvent(byte id) { this.spawner.onEventTriggered(level(), id); }
/*    */ 
/*    */ 
/*    */   
/*    */   public void tick() {
/* 71 */     super.tick();
/* 72 */     this.ticker.run();
/*    */   }
/*    */ 
/*    */   
/* 76 */   public BaseSpawner getSpawner() { return this.spawner; }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\entity\vehicle\minecart\MinecartSpawner.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */