/*     */ package net.minecraft.world.entity.vehicle.minecart;
/*     */ 
/*     */ import java.util.List;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.server.level.ServerLevel;
/*     */ import net.minecraft.world.entity.EntitySelector;
/*     */ import net.minecraft.world.entity.EntityType;
/*     */ import net.minecraft.world.entity.item.ItemEntity;
/*     */ import net.minecraft.world.entity.player.Inventory;
/*     */ import net.minecraft.world.inventory.AbstractContainerMenu;
/*     */ import net.minecraft.world.inventory.HopperMenu;
/*     */ import net.minecraft.world.item.Item;
/*     */ import net.minecraft.world.item.ItemStack;
/*     */ import net.minecraft.world.item.Items;
/*     */ import net.minecraft.world.level.Level;
/*     */ import net.minecraft.world.level.block.Blocks;
/*     */ import net.minecraft.world.level.block.entity.Hopper;
/*     */ import net.minecraft.world.level.block.entity.HopperBlockEntity;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ import net.minecraft.world.level.block.state.properties.RailShape;
/*     */ import net.minecraft.world.level.storage.ValueInput;
/*     */ import net.minecraft.world.level.storage.ValueOutput;
/*     */ 
/*     */ public class MinecartHopper
/*     */   extends AbstractMinecartContainer
/*     */   implements Hopper
/*     */ {
/*     */   private static final boolean DEFAULT_ENABLED = true;
/*     */   private boolean enabled = true;
/*     */   private boolean consumedItemThisFrame = false;
/*     */   
/*  32 */   public MinecartHopper(EntityType<? extends MinecartHopper> type, Level level) { super(type, level); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  37 */   public BlockState getDefaultDisplayBlockState() { return Blocks.HOPPER.defaultBlockState(); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  42 */   public int getDefaultDisplayOffset() { return 1; }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  47 */   public int getContainerSize() { return 5; }
/*     */ 
/*     */ 
/*     */   
/*     */   public void activateMinecart(ServerLevel level, int xt, int yt, int zt, boolean state) {
/*  52 */     boolean newEnabled = !state;
/*     */     
/*  54 */     if (newEnabled != isEnabled()) {
/*  55 */       setEnabled(newEnabled);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*  60 */   public boolean isEnabled() { return this.enabled; }
/*     */ 
/*     */ 
/*     */   
/*  64 */   public void setEnabled(boolean enabled) { this.enabled = enabled; }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  69 */   public double getLevelX() { return getX(); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  74 */   public double getLevelY() { return getY() + 0.5D; }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  79 */   public double getLevelZ() { return getZ(); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  84 */   public boolean isGridAligned() { return false; }
/*     */ 
/*     */ 
/*     */   
/*     */   public void tick() {
/*  89 */     this.consumedItemThisFrame = false;
/*  90 */     super.tick();
/*  91 */     tryConsumeItems();
/*     */   }
/*     */ 
/*     */   
/*     */   protected double makeStepAlongTrack(BlockPos pos, RailShape shape, double movementLeft) {
/*  96 */     double left = super.makeStepAlongTrack(pos, shape, movementLeft);
/*  97 */     tryConsumeItems();
/*  98 */     return left;
/*     */   }
/*     */   
/*     */   private void tryConsumeItems() {
/* 102 */     if (!level().isClientSide() && isAlive() && isEnabled() && !this.consumedItemThisFrame && 
/* 103 */       suckInItems()) {
/* 104 */       this.consumedItemThisFrame = true;
/* 105 */       setChanged();
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean suckInItems() {
/* 111 */     if (HopperBlockEntity.suckInItems(level(), this)) {
/* 112 */       return true;
/*     */     }
/*     */     
/* 115 */     List<ItemEntity> entities = level().getEntitiesOfClass(ItemEntity.class, getBoundingBox().inflate(0.25D, 0.0D, 0.25D), EntitySelector.ENTITY_STILL_ALIVE);
/*     */     
/* 117 */     for (ItemEntity entity : entities) {
/* 118 */       if (HopperBlockEntity.addItem(this, entity)) {
/* 119 */         return true;
/*     */       }
/*     */     } 
/*     */     
/* 123 */     return false;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/* 128 */   protected Item getDropItem() { return Items.HOPPER_MINECART; }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 133 */   public ItemStack getPickResult() { return new ItemStack(Items.HOPPER_MINECART); }
/*     */ 
/*     */ 
/*     */   
/*     */   protected void addAdditionalSaveData(ValueOutput output) {
/* 138 */     super.addAdditionalSaveData(output);
/* 139 */     output.putBoolean("Enabled", this.enabled);
/*     */   }
/*     */ 
/*     */   
/*     */   protected void readAdditionalSaveData(ValueInput input) {
/* 144 */     super.readAdditionalSaveData(input);
/* 145 */     this.enabled = input.getBooleanOr("Enabled", true);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/* 150 */   public AbstractContainerMenu createMenu(int containerId, Inventory inventory) { return new HopperMenu(containerId, inventory, this); }
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\entity\vehicle\minecart\MinecartHopper.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */