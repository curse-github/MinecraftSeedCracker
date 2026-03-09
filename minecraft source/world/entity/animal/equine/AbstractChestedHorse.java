/*     */ package net.minecraft.world.entity.animal.equine;
/*     */ 
/*     */ import java.util.Objects;
/*     */ import net.minecraft.network.syncher.EntityDataAccessor;
/*     */ import net.minecraft.network.syncher.EntityDataSerializers;
/*     */ import net.minecraft.network.syncher.SynchedEntityData;
/*     */ import net.minecraft.server.level.ServerLevel;
/*     */ import net.minecraft.sounds.SoundEvents;
/*     */ import net.minecraft.util.RandomSource;
/*     */ import net.minecraft.world.InteractionHand;
/*     */ import net.minecraft.world.InteractionResult;
/*     */ import net.minecraft.world.ItemStackWithSlot;
/*     */ import net.minecraft.world.entity.EntityAttachment;
/*     */ import net.minecraft.world.entity.EntityAttachments;
/*     */ import net.minecraft.world.entity.EntityDimensions;
/*     */ import net.minecraft.world.entity.EntityType;
/*     */ import net.minecraft.world.entity.Leashable;
/*     */ import net.minecraft.world.entity.Pose;
/*     */ import net.minecraft.world.entity.SlotAccess;
/*     */ import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
/*     */ import net.minecraft.world.entity.ai.attributes.Attributes;
/*     */ import net.minecraft.world.entity.player.Player;
/*     */ import net.minecraft.world.item.ItemStack;
/*     */ import net.minecraft.world.item.Items;
/*     */ import net.minecraft.world.level.Level;
/*     */ import net.minecraft.world.level.block.Blocks;
/*     */ import net.minecraft.world.level.storage.ValueInput;
/*     */ import net.minecraft.world.level.storage.ValueOutput;
/*     */ import net.minecraft.world.phys.Vec3;
/*     */ 
/*     */ public abstract class AbstractChestedHorse extends AbstractHorse {
/*  32 */   private static final EntityDataAccessor<Boolean> DATA_ID_CHEST = SynchedEntityData.defineId(AbstractChestedHorse.class, EntityDataSerializers.BOOLEAN);
/*     */   
/*     */   private static final boolean DEFAULT_HAS_CHEST = false;
/*     */   private final EntityDimensions babyDimensions;
/*     */   
/*     */   protected AbstractChestedHorse(EntityType<? extends AbstractChestedHorse> type, Level level) {
/*  38 */     super(type, level);
/*     */     
/*  40 */     this.canGallop = false;
/*  41 */     this
/*     */ 
/*     */ 
/*     */       
/*  45 */       .babyDimensions = type.getDimensions().withAttachments(EntityAttachments.builder().attach(EntityAttachment.PASSENGER, 0.0F, type.getHeight() - 0.15625F, 0.0F)).scale(0.5F);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*  50 */   protected void randomizeAttributes(RandomSource random) { Objects.requireNonNull(random); getAttribute(Attributes.MAX_HEALTH).setBaseValue(generateMaxHealth(random::nextInt)); }
/*     */ 
/*     */ 
/*     */   
/*     */   protected void defineSynchedData(SynchedEntityData.Builder entityData) {
/*  55 */     super.defineSynchedData(entityData);
/*     */     
/*  57 */     entityData.define(DATA_ID_CHEST, Boolean.valueOf(false));
/*     */   }
/*     */   
/*     */   public static AttributeSupplier.Builder createBaseChestedHorseAttributes() {
/*  61 */     return createBaseHorseAttributes()
/*  62 */       .add(Attributes.MOVEMENT_SPEED, 0.17499999701976776D)
/*  63 */       .add(Attributes.JUMP_STRENGTH, 0.5D);
/*     */   }
/*     */ 
/*     */   
/*  67 */   public boolean hasChest() { return ((Boolean)this.entityData.get(DATA_ID_CHEST)).booleanValue(); }
/*     */ 
/*     */ 
/*     */   
/*  71 */   public void setChest(boolean flag) { this.entityData.set(DATA_ID_CHEST, Boolean.valueOf(flag)); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  76 */   public EntityDimensions getDefaultDimensions(Pose pose) { return isBaby() ? this.babyDimensions : super.getDefaultDimensions(pose); }
/*     */ 
/*     */ 
/*     */   
/*     */   protected void dropEquipment(ServerLevel level) {
/*  81 */     super.dropEquipment(level);
/*  82 */     if (hasChest()) {
/*  83 */       spawnAtLocation(level, Blocks.CHEST);
/*  84 */       setChest(false);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   protected void addAdditionalSaveData(ValueOutput output) {
/*  90 */     super.addAdditionalSaveData(output);
/*     */     
/*  92 */     output.putBoolean("ChestedHorse", hasChest());
/*  93 */     if (hasChest()) {
/*  94 */       ValueOutput.TypedOutputList<ItemStackWithSlot> items = output.list("Items", ItemStackWithSlot.CODEC);
/*  95 */       for (int i = 0; i < this.inventory.getContainerSize(); i++) {
/*  96 */         ItemStack stack = this.inventory.getItem(i);
/*     */         
/*  98 */         if (!stack.isEmpty()) {
/*  99 */           items.add(new ItemStackWithSlot(i, stack));
/*     */         }
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   protected void readAdditionalSaveData(ValueInput input) {
/* 107 */     super.readAdditionalSaveData(input);
/*     */     
/* 109 */     setChest(input.getBooleanOr("ChestedHorse", false));
/*     */ 
/*     */     
/* 112 */     createInventory();
/*     */     
/* 114 */     if (hasChest()) {
/* 115 */       for (ItemStackWithSlot item : input.listOrEmpty("Items", ItemStackWithSlot.CODEC)) {
/* 116 */         if (item.isValidInContainer(this.inventory.getContainerSize())) {
/* 117 */           this.inventory.setItem(item.slot(), item.stack());
/*     */         }
/*     */       } 
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public SlotAccess getSlot(int slot) {
/* 125 */     if (slot == 499) {
/* 126 */       return new SlotAccess()
/*     */         {
/*     */           public ItemStack get() {
/* 129 */             return AbstractChestedHorse.this.hasChest() ? new ItemStack(Items.CHEST) : ItemStack.EMPTY;
/*     */           }
/*     */ 
/*     */           
/*     */           public boolean set(ItemStack itemStack) {
/* 134 */             if (itemStack.isEmpty()) {
/* 135 */               if (AbstractChestedHorse.this.hasChest()) {
/* 136 */                 AbstractChestedHorse.this.setChest(false);
/* 137 */                 AbstractChestedHorse.this.createInventory();
/*     */               } 
/* 139 */               return true;
/*     */             } 
/* 141 */             if (itemStack.is(Items.CHEST)) {
/* 142 */               if (!AbstractChestedHorse.this.hasChest()) {
/* 143 */                 AbstractChestedHorse.this.setChest(true);
/* 144 */                 AbstractChestedHorse.this.createInventory();
/*     */               } 
/* 146 */               return true;
/*     */             } 
/* 148 */             return false;
/*     */           }
/*     */         };
/*     */     }
/* 152 */     return super.getSlot(slot);
/*     */   }
/*     */ 
/*     */   
/*     */   public InteractionResult mobInteract(Player player, InteractionHand hand) {
/* 157 */     boolean shouldOpenInventory = (!isBaby() && isTamed() && player.isSecondaryUseActive());
/* 158 */     if (isVehicle() || shouldOpenInventory) {
/* 159 */       return super.mobInteract(player, hand);
/*     */     }
/*     */     
/* 162 */     ItemStack itemStack = player.getItemInHand(hand);
/* 163 */     if (!itemStack.isEmpty()) {
/* 164 */       if (isFood(itemStack)) {
/* 165 */         return fedFood(player, itemStack);
/*     */       }
/*     */       
/* 168 */       if (!isTamed()) {
/* 169 */         makeMad();
/* 170 */         return InteractionResult.SUCCESS;
/*     */       } 
/*     */       
/* 173 */       if (!hasChest() && itemStack.is(Items.CHEST)) {
/* 174 */         equipChest(player, itemStack);
/* 175 */         return InteractionResult.SUCCESS;
/*     */       } 
/*     */     } 
/* 178 */     return super.mobInteract(player, hand);
/*     */   }
/*     */   
/*     */   private void equipChest(Player player, ItemStack itemStack) {
/* 182 */     setChest(true);
/* 183 */     playChestEquipsSound();
/* 184 */     itemStack.consume(1, player);
/* 185 */     createInventory();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/* 190 */   public Vec3[] getQuadLeashOffsets() { return Leashable.createQuadLeashOffsets(this, 0.04D, 0.41D, 0.18D, 0.73D); }
/*     */ 
/*     */ 
/*     */   
/* 194 */   protected void playChestEquipsSound() { playSound(SoundEvents.DONKEY_CHEST, 1.0F, (this.random.nextFloat() - this.random.nextFloat()) * 0.2F + 1.0F); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 199 */   public int getInventoryColumns() { return hasChest() ? 5 : 0; }
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\entity\animal\equine\AbstractChestedHorse.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */