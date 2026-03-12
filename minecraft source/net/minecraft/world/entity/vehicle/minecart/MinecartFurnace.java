/*     */ package net.minecraft.world.entity.vehicle.minecart;
/*     */ 
/*     */ import net.minecraft.core.Direction;
/*     */ import net.minecraft.core.particles.ParticleTypes;
/*     */ import net.minecraft.network.syncher.EntityDataAccessor;
/*     */ import net.minecraft.network.syncher.EntityDataSerializers;
/*     */ import net.minecraft.network.syncher.SynchedEntityData;
/*     */ import net.minecraft.server.level.ServerLevel;
/*     */ import net.minecraft.tags.ItemTags;
/*     */ import net.minecraft.world.InteractionHand;
/*     */ import net.minecraft.world.InteractionResult;
/*     */ import net.minecraft.world.entity.EntityType;
/*     */ import net.minecraft.world.entity.player.Player;
/*     */ import net.minecraft.world.item.Item;
/*     */ import net.minecraft.world.item.ItemStack;
/*     */ import net.minecraft.world.item.Items;
/*     */ import net.minecraft.world.level.Level;
/*     */ import net.minecraft.world.level.block.Blocks;
/*     */ import net.minecraft.world.level.block.FurnaceBlock;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ import net.minecraft.world.level.storage.ValueInput;
/*     */ import net.minecraft.world.level.storage.ValueOutput;
/*     */ import net.minecraft.world.phys.Vec3;
/*     */ 
/*     */ public class MinecartFurnace
/*     */   extends AbstractMinecart
/*     */ {
/*  28 */   private static final EntityDataAccessor<Boolean> DATA_ID_FUEL = SynchedEntityData.defineId(MinecartFurnace.class, EntityDataSerializers.BOOLEAN);
/*     */   
/*     */   private static final int FUEL_TICKS_PER_ITEM = 3600;
/*     */   private static final int MAX_FUEL_TICKS = 32000;
/*     */   private static final short DEFAULT_FUEL = 0;
/*  33 */   private static final Vec3 DEFAULT_PUSH = Vec3.ZERO;
/*     */   
/*  35 */   private int fuel = 0;
/*  36 */   public Vec3 push = DEFAULT_PUSH;
/*     */ 
/*     */   
/*  39 */   public MinecartFurnace(EntityType<? extends MinecartFurnace> type, Level level) { super(type, level); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  44 */   public boolean isFurnace() { return true; }
/*     */ 
/*     */ 
/*     */   
/*     */   protected void defineSynchedData(SynchedEntityData.Builder entityData) {
/*  49 */     super.defineSynchedData(entityData);
/*  50 */     entityData.define(DATA_ID_FUEL, Boolean.valueOf(false));
/*     */   }
/*     */ 
/*     */   
/*     */   public void tick() {
/*  55 */     super.tick();
/*     */     
/*  57 */     if (!level().isClientSide()) {
/*  58 */       if (this.fuel > 0) {
/*  59 */         this.fuel--;
/*     */       }
/*  61 */       if (this.fuel <= 0) {
/*  62 */         this.push = Vec3.ZERO;
/*     */       }
/*  64 */       setHasFuel((this.fuel > 0));
/*     */     } 
/*     */     
/*  67 */     if (hasFuel() && this.random.nextInt(4) == 0) {
/*  68 */       level().addParticle(ParticleTypes.LARGE_SMOKE, getX(), getY() + 0.8D, getZ(), 0.0D, 0.0D, 0.0D);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*  74 */   protected double getMaxSpeed(ServerLevel level) { return isInWater() ? (super.getMaxSpeed(level) * 0.75D) : (super.getMaxSpeed(level) * 0.5D); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  79 */   protected Item getDropItem() { return Items.FURNACE_MINECART; }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  84 */   public ItemStack getPickResult() { return new ItemStack(Items.FURNACE_MINECART); }
/*     */ 
/*     */ 
/*     */   
/*     */   protected Vec3 applyNaturalSlowdown(Vec3 deltaMovement) {
/*     */     Vec3 newDeltaMovement;
/*  90 */     if (this.push.lengthSqr() > 1.0E-7D) {
/*  91 */       this.push = calculateNewPushAlong(deltaMovement);
/*     */ 
/*     */ 
/*     */       
/*  95 */       newDeltaMovement = deltaMovement.multiply(0.8D, 0.0D, 0.8D).add(this.push);
/*     */       
/*  97 */       if (isInWater()) {
/*  98 */         newDeltaMovement = newDeltaMovement.scale(0.1D);
/*     */       }
/*     */     } else {
/* 101 */       newDeltaMovement = deltaMovement.multiply(0.98D, 0.0D, 0.98D);
/*     */     } 
/*     */     
/* 104 */     return super.applyNaturalSlowdown(newDeltaMovement);
/*     */   }
/*     */ 
/*     */   
/*     */   private Vec3 calculateNewPushAlong(Vec3 deltaMovement) {
/* 109 */     double epsilonPushCheck = 1.0E-4D;
/* 110 */     double epsilonMovementCheck = 0.001D;
/* 111 */     if (this.push.horizontalDistanceSqr() > 1.0E-4D && deltaMovement.horizontalDistanceSqr() > 0.001D)
/*     */     {
/* 113 */       return this.push.projectedOn(deltaMovement).normalize().scale(this.push.length());
/*     */     }
/* 115 */     return this.push;
/*     */   }
/*     */ 
/*     */   
/*     */   public InteractionResult interact(Player player, InteractionHand hand) {
/* 120 */     ItemStack itemStack = player.getItemInHand(hand);
/* 121 */     if (addFuel(player.position(), itemStack)) {
/* 122 */       itemStack.consume(1, player);
/*     */     }
/*     */     
/* 125 */     return InteractionResult.SUCCESS;
/*     */   }
/*     */   
/*     */   public boolean addFuel(Vec3 interactingPos, ItemStack itemStack) {
/* 129 */     if (itemStack.is(ItemTags.FURNACE_MINECART_FUEL) && this.fuel + 3600 <= 32000) {
/* 130 */       this.fuel += 3600;
/*     */     } else {
/* 132 */       return false;
/*     */     } 
/*     */     
/* 135 */     if (this.fuel > 0) {
/* 136 */       this.push = position().subtract(interactingPos).horizontal();
/*     */     }
/*     */     
/* 139 */     return true;
/*     */   }
/*     */ 
/*     */   
/*     */   protected void addAdditionalSaveData(ValueOutput output) {
/* 144 */     super.addAdditionalSaveData(output);
/* 145 */     output.putDouble("PushX", this.push.x);
/* 146 */     output.putDouble("PushZ", this.push.z);
/* 147 */     output.putShort("Fuel", (short)this.fuel);
/*     */   }
/*     */ 
/*     */   
/*     */   protected void readAdditionalSaveData(ValueInput input) {
/* 152 */     super.readAdditionalSaveData(input);
/* 153 */     double xPush = input.getDoubleOr("PushX", DEFAULT_PUSH.x);
/* 154 */     double zPush = input.getDoubleOr("PushZ", DEFAULT_PUSH.z);
/* 155 */     this.push = new Vec3(xPush, 0.0D, zPush);
/* 156 */     this.fuel = input.getShortOr("Fuel", (short)0);
/*     */   }
/*     */ 
/*     */   
/* 160 */   protected boolean hasFuel() { return ((Boolean)this.entityData.get(DATA_ID_FUEL)).booleanValue(); }
/*     */ 
/*     */ 
/*     */   
/* 164 */   protected void setHasFuel(boolean fuel) { this.entityData.set(DATA_ID_FUEL, Boolean.valueOf(fuel)); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 169 */   public BlockState getDefaultDisplayBlockState() { return (BlockState)((BlockState)Blocks.FURNACE.defaultBlockState().setValue(FurnaceBlock.FACING, Direction.NORTH)).setValue(FurnaceBlock.LIT, Boolean.valueOf(hasFuel())); }
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\entity\vehicle\minecart\MinecartFurnace.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */