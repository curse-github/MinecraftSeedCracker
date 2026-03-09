/*     */ package net.minecraft.world.level.block.entity;
/*     */ 
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.HolderLookup;
/*     */ import net.minecraft.core.component.DataComponentGetter;
/*     */ import net.minecraft.core.component.DataComponentMap;
/*     */ import net.minecraft.core.component.DataComponents;
/*     */ import net.minecraft.nbt.CompoundTag;
/*     */ import net.minecraft.network.chat.Component;
/*     */ import net.minecraft.network.chat.ComponentSerialization;
/*     */ import net.minecraft.network.protocol.Packet;
/*     */ import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
/*     */ import net.minecraft.resources.Identifier;
/*     */ import net.minecraft.world.item.component.ResolvableProfile;
/*     */ import net.minecraft.world.level.Level;
/*     */ import net.minecraft.world.level.block.SkullBlock;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ import net.minecraft.world.level.storage.ValueInput;
/*     */ import net.minecraft.world.level.storage.ValueOutput;
/*     */ 
/*     */ public class SkullBlockEntity
/*     */   extends BlockEntity
/*     */ {
/*     */   private static final String TAG_PROFILE = "profile";
/*     */   private static final String TAG_NOTE_BLOCK_SOUND = "note_block_sound";
/*     */   private static final String TAG_CUSTOM_NAME = "custom_name";
/*     */   private ResolvableProfile owner;
/*     */   private Identifier noteBlockSound;
/*     */   private int animationTickCount;
/*     */   private boolean isAnimating;
/*     */   private Component customName;
/*     */   
/*  33 */   public SkullBlockEntity(BlockPos worldPosition, BlockState blockState) { super(BlockEntityType.SKULL, worldPosition, blockState); }
/*     */ 
/*     */ 
/*     */   
/*     */   protected void saveAdditional(ValueOutput output) {
/*  38 */     super.saveAdditional(output);
/*     */     
/*  40 */     output.storeNullable("profile", ResolvableProfile.CODEC, this.owner);
/*  41 */     output.storeNullable("note_block_sound", Identifier.CODEC, this.noteBlockSound);
/*  42 */     output.storeNullable("custom_name", ComponentSerialization.CODEC, this.customName);
/*     */   }
/*     */ 
/*     */   
/*     */   protected void loadAdditional(ValueInput input) {
/*  47 */     super.loadAdditional(input);
/*     */     
/*  49 */     this.owner = (ResolvableProfile)input.read("profile", ResolvableProfile.CODEC).orElse(null);
/*  50 */     this.noteBlockSound = (Identifier)input.read("note_block_sound", Identifier.CODEC).orElse(null);
/*  51 */     this.customName = parseCustomNameSafe(input, "custom_name");
/*     */   }
/*     */   
/*     */   public static void animation(Level level, BlockPos pos, BlockState state, SkullBlockEntity entity) {
/*  55 */     if (state.hasProperty(SkullBlock.POWERED) && ((Boolean)state.getValue(SkullBlock.POWERED)).booleanValue()) {
/*  56 */       entity.isAnimating = true;
/*  57 */       entity.animationTickCount++;
/*     */     } else {
/*  59 */       entity.isAnimating = false;
/*     */     } 
/*     */   }
/*     */   
/*     */   public float getAnimation(float a) {
/*  64 */     if (this.isAnimating) {
/*  65 */       return this.animationTickCount + a;
/*     */     }
/*  67 */     return this.animationTickCount;
/*     */   }
/*     */ 
/*     */   
/*  71 */   public ResolvableProfile getOwnerProfile() { return this.owner; }
/*     */ 
/*     */ 
/*     */   
/*  75 */   public Identifier getNoteBlockSound() { return this.noteBlockSound; }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  80 */   public ClientboundBlockEntityDataPacket getUpdatePacket() { return ClientboundBlockEntityDataPacket.create(this); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  85 */   public CompoundTag getUpdateTag(HolderLookup.Provider registries) { return saveCustomOnly(registries); }
/*     */ 
/*     */ 
/*     */   
/*     */   protected void applyImplicitComponents(DataComponentGetter components) {
/*  90 */     super.applyImplicitComponents(components);
/*  91 */     this.owner = (ResolvableProfile)components.get(DataComponents.PROFILE);
/*  92 */     this.noteBlockSound = (Identifier)components.get(DataComponents.NOTE_BLOCK_SOUND);
/*  93 */     this.customName = (Component)components.get(DataComponents.CUSTOM_NAME);
/*     */   }
/*     */ 
/*     */   
/*     */   protected void collectImplicitComponents(DataComponentMap.Builder components) {
/*  98 */     super.collectImplicitComponents(components);
/*  99 */     components.set(DataComponents.PROFILE, this.owner);
/* 100 */     components.set(DataComponents.NOTE_BLOCK_SOUND, this.noteBlockSound);
/* 101 */     components.set(DataComponents.CUSTOM_NAME, this.customName);
/*     */   }
/*     */ 
/*     */   
/*     */   public void removeComponentsFromTag(ValueOutput output) {
/* 106 */     super.removeComponentsFromTag(output);
/* 107 */     output.discard("profile");
/* 108 */     output.discard("note_block_sound");
/* 109 */     output.discard("custom_name");
/*     */   }
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\level\block\entity\SkullBlockEntity.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */