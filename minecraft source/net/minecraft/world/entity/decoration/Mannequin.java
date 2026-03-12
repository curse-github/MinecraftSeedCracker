/*     */ package net.minecraft.world.entity.decoration;
/*     */ 
/*     */ import com.mojang.serialization.Codec;
/*     */ import com.mojang.serialization.DataResult;
/*     */ import java.util.Arrays;
/*     */ import java.util.List;
/*     */ import java.util.Optional;
/*     */ import java.util.Set;
/*     */ import net.minecraft.core.component.DataComponentGetter;
/*     */ import net.minecraft.core.component.DataComponentType;
/*     */ import net.minecraft.core.component.DataComponents;
/*     */ import net.minecraft.network.chat.Component;
/*     */ import net.minecraft.network.chat.ComponentSerialization;
/*     */ import net.minecraft.network.syncher.EntityDataAccessor;
/*     */ import net.minecraft.network.syncher.EntityDataSerializers;
/*     */ import net.minecraft.network.syncher.SynchedEntityData;
/*     */ import net.minecraft.world.entity.Avatar;
/*     */ import net.minecraft.world.entity.EntityType;
/*     */ import net.minecraft.world.entity.HumanoidArm;
/*     */ import net.minecraft.world.entity.Pose;
/*     */ import net.minecraft.world.entity.player.PlayerModelPart;
/*     */ import net.minecraft.world.item.component.ResolvableProfile;
/*     */ import net.minecraft.world.level.Level;
/*     */ import net.minecraft.world.level.storage.ValueInput;
/*     */ import net.minecraft.world.level.storage.ValueOutput;
/*     */ 
/*     */ public class Mannequin
/*     */   extends Avatar {
/*  29 */   protected static final EntityDataAccessor<ResolvableProfile> DATA_PROFILE = SynchedEntityData.defineId(Mannequin.class, EntityDataSerializers.RESOLVABLE_PROFILE);
/*  30 */   private static final EntityDataAccessor<Boolean> DATA_IMMOVABLE = SynchedEntityData.defineId(Mannequin.class, EntityDataSerializers.BOOLEAN);
/*  31 */   private static final EntityDataAccessor<Optional<Component>> DATA_DESCRIPTION = SynchedEntityData.defineId(Mannequin.class, EntityDataSerializers.OPTIONAL_COMPONENT);
/*     */   
/*  33 */   private static final byte ALL_LAYERS = (byte)Arrays.stream(PlayerModelPart.values()).mapToInt(PlayerModelPart::getMask).reduce(0, (a, b) -> a | b);
/*     */   
/*  35 */   private static final Set<Pose> VALID_POSES = Set.of(Pose.STANDING, Pose.CROUCHING, Pose.SWIMMING, Pose.FALL_FLYING, Pose.SLEEPING);
/*  36 */   public static final Codec<Pose> POSE_CODEC = Pose.CODEC.validate(pose -> VALID_POSES.contains(pose) ? DataResult.success(pose) : DataResult.error(()));
/*     */   
/*  38 */   private static final Codec<Byte> LAYERS_CODEC = PlayerModelPart.CODEC.listOf().xmap(list -> 
/*  39 */       Byte.valueOf((byte)list.stream().mapToInt(PlayerModelPart::getMask).reduce(ALL_LAYERS, ())), mask -> 
/*  40 */       Arrays.stream(PlayerModelPart.values()).filter(()).toList());
/*     */ 
/*     */ 
/*     */   
/*  44 */   public static final ResolvableProfile DEFAULT_PROFILE = ResolvableProfile.Static.EMPTY;
/*     */   
/*  46 */   private static final Component DEFAULT_DESCRIPTION = Component.translatable("entity.minecraft.mannequin.label");
/*     */   
/*  48 */   protected static EntityType.EntityFactory<Mannequin> constructor = Mannequin::new;
/*     */   
/*     */   private static final String PROFILE_FIELD = "profile";
/*     */   
/*     */   private static final String HIDDEN_LAYERS_FIELD = "hidden_layers";
/*     */   private static final String MAIN_HAND_FIELD = "main_hand";
/*     */   private static final String POSE_FIELD = "pose";
/*     */   private static final String IMMOVABLE_FIELD = "immovable";
/*     */   private static final String DESCRIPTION_FIELD = "description";
/*     */   private static final String HIDE_DESCRIPTION_FIELD = "hide_description";
/*  58 */   private Component description = DEFAULT_DESCRIPTION;
/*     */   private boolean hideDescription = false;
/*     */   
/*     */   public Mannequin(EntityType<Mannequin> type, Level level) {
/*  62 */     super(type, level);
/*  63 */     this.entityData.set(DATA_PLAYER_MODE_CUSTOMISATION, Byte.valueOf(ALL_LAYERS));
/*     */   }
/*     */ 
/*     */   
/*  67 */   protected Mannequin(Level level) { this(EntityType.MANNEQUIN, level); }
/*     */ 
/*     */ 
/*     */   
/*  71 */   public static Mannequin create(EntityType<Mannequin> type, Level level) { return (Mannequin)constructor.create(type, level); }
/*     */ 
/*     */ 
/*     */   
/*     */   protected void defineSynchedData(SynchedEntityData.Builder entityData) {
/*  76 */     super.defineSynchedData(entityData);
/*     */     
/*  78 */     entityData.define(DATA_PROFILE, DEFAULT_PROFILE);
/*  79 */     entityData.define(DATA_IMMOVABLE, Boolean.valueOf(false));
/*  80 */     entityData.define(DATA_DESCRIPTION, Optional.of(DEFAULT_DESCRIPTION));
/*     */   }
/*     */ 
/*     */   
/*  84 */   protected ResolvableProfile getProfile() { return (ResolvableProfile)this.entityData.get(DATA_PROFILE); }
/*     */ 
/*     */ 
/*     */   
/*  88 */   private void setProfile(ResolvableProfile profile) { this.entityData.set(DATA_PROFILE, profile); }
/*     */ 
/*     */ 
/*     */   
/*  92 */   private boolean getImmovable() { return ((Boolean)this.entityData.get(DATA_IMMOVABLE)).booleanValue(); }
/*     */ 
/*     */ 
/*     */   
/*  96 */   private void setImmovable(boolean immovable) { this.entityData.set(DATA_IMMOVABLE, Boolean.valueOf(immovable)); }
/*     */ 
/*     */ 
/*     */   
/* 100 */   protected Component getDescription() { return (Component)((Optional)this.entityData.get(DATA_DESCRIPTION)).orElse(null); }
/*     */ 
/*     */   
/*     */   private void setDescription(Component description) {
/* 104 */     this.description = description;
/* 105 */     updateDescription();
/*     */   }
/*     */   
/*     */   private void setHideDescription(boolean hideDescription) {
/* 109 */     this.hideDescription = hideDescription;
/* 110 */     updateDescription();
/*     */   }
/*     */ 
/*     */   
/* 114 */   private void updateDescription() { this.entityData.set(DATA_DESCRIPTION, this.hideDescription ? Optional.empty() : Optional.of(this.description)); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 119 */   protected boolean isImmobile() { return (getImmovable() || super.isImmobile()); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 124 */   public boolean isEffectiveAi() { return (!getImmovable() && super.isEffectiveAi()); }
/*     */ 
/*     */ 
/*     */   
/*     */   protected void addAdditionalSaveData(ValueOutput output) {
/* 129 */     super.addAdditionalSaveData(output);
/*     */     
/* 131 */     output.store("profile", ResolvableProfile.CODEC, getProfile());
/* 132 */     output.store("hidden_layers", LAYERS_CODEC, (Byte)this.entityData.get(DATA_PLAYER_MODE_CUSTOMISATION));
/* 133 */     output.store("main_hand", HumanoidArm.CODEC, getMainArm());
/* 134 */     output.store("pose", POSE_CODEC, getPose());
/* 135 */     output.putBoolean("immovable", getImmovable());
/* 136 */     Component description = getDescription();
/* 137 */     if (description != null) {
/* 138 */       if (!description.equals(DEFAULT_DESCRIPTION)) {
/* 139 */         output.store("description", ComponentSerialization.CODEC, description);
/*     */       }
/*     */     } else {
/* 142 */       output.putBoolean("hide_description", true);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   protected void readAdditionalSaveData(ValueInput input) {
/* 148 */     super.readAdditionalSaveData(input);
/*     */     
/* 150 */     input.read("profile", ResolvableProfile.CODEC).ifPresent(this::setProfile);
/* 151 */     this.entityData.set(DATA_PLAYER_MODE_CUSTOMISATION, (Byte)input.read("hidden_layers", LAYERS_CODEC).orElse(Byte.valueOf(ALL_LAYERS)));
/* 152 */     setMainArm((HumanoidArm)input.read("main_hand", HumanoidArm.CODEC).orElse(DEFAULT_MAIN_HAND));
/* 153 */     setPose((Pose)input.read("pose", POSE_CODEC).orElse(Pose.STANDING));
/* 154 */     setImmovable(input.getBooleanOr("immovable", false));
/* 155 */     setHideDescription(input.getBooleanOr("hide_description", false));
/* 156 */     setDescription((Component)input.read("description", ComponentSerialization.CODEC).orElse(DEFAULT_DESCRIPTION));
/*     */   }
/*     */ 
/*     */   
/*     */   public <T> T get(DataComponentType<? extends T> type) {
/* 161 */     if (type == DataComponents.PROFILE) {
/* 162 */       return (T)castComponentValue(type, getProfile());
/*     */     }
/*     */     
/* 165 */     return (T)super.get(type);
/*     */   }
/*     */ 
/*     */   
/*     */   protected void applyImplicitComponents(DataComponentGetter components) {
/* 170 */     applyImplicitComponentIfPresent(components, DataComponents.PROFILE);
/* 171 */     super.applyImplicitComponents(components);
/*     */   }
/*     */ 
/*     */   
/*     */   protected <T> boolean applyImplicitComponent(DataComponentType<T> type, T value) {
/* 176 */     if (type == DataComponents.PROFILE) {
/* 177 */       setProfile((ResolvableProfile)castComponentValue(DataComponents.PROFILE, value));
/* 178 */       return true;
/*     */     } 
/*     */     
/* 181 */     return super.applyImplicitComponent(type, value);
/*     */   }
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\entity\decoration\Mannequin.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */