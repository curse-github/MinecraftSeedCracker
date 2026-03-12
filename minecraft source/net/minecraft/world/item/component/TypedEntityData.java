/*     */ package net.minecraft.world.item.component;
/*     */ 
/*     */ import com.mojang.datafixers.util.Pair;
/*     */ import com.mojang.logging.LogUtils;
/*     */ import com.mojang.serialization.Codec;
/*     */ import com.mojang.serialization.DataResult;
/*     */ import com.mojang.serialization.DynamicOps;
/*     */ import java.util.UUID;
/*     */ import java.util.function.BiFunction;
/*     */ import java.util.function.Consumer;
/*     */ import net.minecraft.ChatFormatting;
/*     */ import net.minecraft.core.HolderLookup;
/*     */ import net.minecraft.core.component.DataComponentGetter;
/*     */ import net.minecraft.nbt.CompoundTag;
/*     */ import net.minecraft.nbt.NbtOps;
/*     */ import net.minecraft.nbt.Tag;
/*     */ import net.minecraft.network.chat.Component;
/*     */ import net.minecraft.network.codec.ByteBufCodecs;
/*     */ import net.minecraft.network.codec.StreamCodec;
/*     */ import net.minecraft.resources.RegistryOps;
/*     */ import net.minecraft.util.ProblemReporter;
/*     */ import net.minecraft.world.entity.Entity;
/*     */ import net.minecraft.world.entity.EntityType;
/*     */ import net.minecraft.world.item.Item;
/*     */ import net.minecraft.world.item.TooltipFlag;
/*     */ import net.minecraft.world.level.block.entity.BlockEntity;
/*     */ import net.minecraft.world.level.storage.TagValueInput;
/*     */ import net.minecraft.world.level.storage.TagValueOutput;
/*     */ import org.slf4j.Logger;
/*     */ 
/*     */ public final class TypedEntityData<IdType>
/*     */   extends Object implements TooltipProvider {
/*  33 */   private static final Logger LOGGER = LogUtils.getLogger(); private static final String TYPE_TAG = "id";
/*     */   private final IdType type;
/*     */   private final CompoundTag tag;
/*     */   
/*     */   public static <T> Codec<TypedEntityData<T>> codec(final Codec<T> typeCodec) {
/*  38 */     return new Codec<TypedEntityData<T>>()
/*     */       {
/*     */         public <V> DataResult<Pair<TypedEntityData<T>, V>> decode(DynamicOps<V> ops, V input)
/*     */         {
/*  42 */           return CustomData.COMPOUND_TAG_CODEC.decode(ops, input).flatMap(pair -> {
/*  43 */                 CompoundTag tagWithoutType = ((CompoundTag)pair.getFirst()).copy();
/*  44 */                 Tag typeTag = tagWithoutType.remove("id");
/*  45 */                 if (typeTag == null) {
/*  46 */                   return DataResult.error(());
/*     */                 }
/*  48 */                 return typeCodec.parse(TypedEntityData.null.asNbtOps(ops), typeTag).map(());
/*     */               });
/*     */         }
/*     */ 
/*     */         
/*     */         public <V> DataResult<V> encode(TypedEntityData<T> input, DynamicOps<V> ops, V prefix) {
/*  54 */           return typeCodec.encodeStart(TypedEntityData.null.asNbtOps(ops), input.type).flatMap(typeTag -> {
/*  55 */                 CompoundTag tag = input.tag.copy();
/*  56 */                 tag.put("id", typeTag);
/*  57 */                 return CustomData.COMPOUND_TAG_CODEC.encode(tag, ops, prefix);
/*     */               });
/*     */         }
/*     */         
/*     */         private static <T> DynamicOps<Tag> asNbtOps(DynamicOps<T> ops) {
/*  62 */           if (ops instanceof RegistryOps) { RegistryOps<T> registryOps = (RegistryOps)ops;
/*  63 */             return registryOps.withParent(NbtOps.INSTANCE); }
/*     */           
/*  65 */           return NbtOps.INSTANCE;
/*     */         }
/*     */       };
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*  72 */   public static <B extends io.netty.buffer.ByteBuf, T> StreamCodec<B, TypedEntityData<T>> streamCodec(StreamCodec<B, T> typeCodec) { return StreamCodec.composite(typeCodec, TypedEntityData::type, ByteBufCodecs.COMPOUND_TAG, TypedEntityData::tag, TypedEntityData::new); }
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
/*     */   private TypedEntityData(IdType type, CompoundTag data) {
/*  84 */     this.type = type;
/*  85 */     this.tag = stripId(data);
/*     */   }
/*     */ 
/*     */   
/*  89 */   public static <T> TypedEntityData<T> of(T type, CompoundTag data) { return new TypedEntityData(type, data); }
/*     */ 
/*     */   
/*     */   private static CompoundTag stripId(CompoundTag tag) {
/*  93 */     if (tag.contains("id")) {
/*  94 */       CompoundTag copy = tag.copy();
/*  95 */       copy.remove("id");
/*  96 */       return copy;
/*     */     } 
/*  98 */     return tag;
/*     */   }
/*     */ 
/*     */   
/* 102 */   public IdType type() { return (IdType)this.type; }
/*     */ 
/*     */ 
/*     */   
/* 106 */   public boolean contains(String name) { return this.tag.contains(name); }
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean equals(Object obj) {
/* 111 */     if (obj == this) {
/* 112 */       return true;
/*     */     }
/* 114 */     if (obj instanceof TypedEntityData) { TypedEntityData<?> customData = (TypedEntityData)obj;
/* 115 */       return (this.type == customData.type && this.tag.equals(customData.tag)); }
/*     */     
/* 117 */     return false;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/* 122 */   public int hashCode() { return 31 * this.type.hashCode() + this.tag.hashCode(); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 127 */   public String toString() { return String.valueOf(this.type) + " " + String.valueOf(this.type); }
/*     */ 
/*     */   
/*     */   public void loadInto(Entity entity) {
/* 131 */     ProblemReporter.ScopedCollector reporter = new ProblemReporter.ScopedCollector(entity.problemPath(), LOGGER); 
/* 132 */     try { TagValueOutput output = TagValueOutput.createWithContext(reporter, entity.registryAccess());
/* 133 */       entity.saveWithoutId(output);
/* 134 */       CompoundTag entityData = output.buildResult();
/*     */       
/* 136 */       UUID uuid = entity.getUUID();
/* 137 */       entityData.merge(getUnsafe());
/* 138 */       entity.load(TagValueInput.create(reporter, entity.registryAccess(), entityData));
/*     */ 
/*     */       
/* 141 */       entity.setUUID(uuid);
/* 142 */       reporter.close(); }
/*     */     catch (Throwable throwable) { try { reporter.close(); }
/*     */       catch (Throwable throwable1) { throwable.addSuppressed(throwable1); }
/*     */        throw throwable; }
/* 146 */      } public boolean loadInto(BlockEntity blockEntity, HolderLookup.Provider registries) { ProblemReporter.ScopedCollector reporter = new ProblemReporter.ScopedCollector(blockEntity.problemPath(), LOGGER); 
/* 147 */     try { TagValueOutput output = TagValueOutput.createWithContext(reporter, registries);
/* 148 */       blockEntity.saveCustomOnly(output);
/* 149 */       CompoundTag entityTag = output.buildResult();
/* 150 */       CompoundTag oldTag = entityTag.copy();
/* 151 */       entityTag.merge(getUnsafe());
/* 152 */       if (!entityTag.equals(oldTag))
/*     */ 
/*     */         
/* 155 */         try { blockEntity.loadCustomOnly(TagValueInput.create(reporter, registries, entityTag));
/* 156 */           blockEntity.setChanged();
/* 157 */           boolean bool1 = true;
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
/* 170 */           reporter.close(); return bool1; } catch (Exception e) { LOGGER.warn("Failed to apply custom data to block entity at {}", blockEntity.getBlockPos(), e); try { blockEntity.loadCustomOnly(TagValueInput.create(reporter.forChild(() -> "(rollback)"), registries, oldTag)); } catch (Exception e2) { LOGGER.warn("Failed to rollback block entity at {} after failure", blockEntity.getBlockPos(), e2); }  }   boolean bool = false; reporter.close(); return bool; }
/*     */     catch (Throwable throwable) { try { reporter.close(); }
/*     */       catch (Throwable throwable1) { throwable.addSuppressed(throwable1); }
/*     */        throw throwable; }
/* 174 */      } private CompoundTag tag() { return this.tag; }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Deprecated
/* 180 */   public CompoundTag getUnsafe() { return this.tag; }
/*     */ 
/*     */ 
/*     */   
/* 184 */   public CompoundTag copyTagWithoutId() { return this.tag.copy(); }
/*     */ 
/*     */ 
/*     */   
/*     */   public void addToTooltip(Item.TooltipContext context, Consumer<Component> consumer, TooltipFlag flag, DataComponentGetter components) {
/* 189 */     if (this.type.getClass() == EntityType.class) {
/* 190 */       EntityType<?> type = (EntityType)this.type;
/* 191 */       if (context.isPeaceful() && !type.isAllowedInPeaceful())
/* 192 */         consumer.accept(Component.translatable("item.spawn_egg.peaceful").withStyle(ChatFormatting.RED)); 
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\item\component\TypedEntityData.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */