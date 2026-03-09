/*     */ package net.minecraft.network.chat;
/*     */ 
/*     */ import com.mojang.datafixers.kinds.App;
/*     */ import com.mojang.datafixers.util.Function3;
/*     */ import com.mojang.serialization.MapCodec;
/*     */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
/*     */ import java.util.Objects;
/*     */ import java.util.Optional;
/*     */ import java.util.UUID;
/*     */ import net.minecraft.core.UUIDUtil;
/*     */ import net.minecraft.core.registries.BuiltInRegistries;
/*     */ import net.minecraft.world.entity.EntityType;
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
/*     */ public class EntityTooltipInfo
/*     */ {
/*  71 */   public static final MapCodec<EntityTooltipInfo> CODEC = RecordCodecBuilder.mapCodec(i -> i.group(BuiltInRegistries.ENTITY_TYPE
/*  72 */         .byNameCodec().fieldOf("id").forGetter(()), UUIDUtil.LENIENT_CODEC
/*  73 */         .fieldOf("uuid").forGetter(()), ComponentSerialization.CODEC
/*  74 */         .optionalFieldOf("name").forGetter(()))
/*  75 */       .apply(i, EntityTooltipInfo::new));
/*     */   
/*     */   public final EntityType<?> type;
/*     */   
/*     */   public final UUID uuid;
/*     */   
/*     */   public final Optional<Component> name;
/*     */   private List<Component> linesCache;
/*     */   
/*  84 */   public EntityTooltipInfo(EntityType<?> type, UUID uuid, Component name) { this(type, uuid, Optional.ofNullable(name)); }
/*     */ 
/*     */   
/*     */   public EntityTooltipInfo(EntityType<?> type, UUID uuid, Optional<Component> name) {
/*  88 */     this.type = type;
/*  89 */     this.uuid = uuid;
/*  90 */     this.name = name;
/*     */   }
/*     */   
/*     */   public List<Component> getTooltipLines() {
/*  94 */     if (this.linesCache == null) {
/*  95 */       this.linesCache = new ArrayList();
/*  96 */       Objects.requireNonNull(this.linesCache); this.name.ifPresent(this.linesCache::add);
/*  97 */       this.linesCache.add(Component.translatable("gui.entity_tooltip.type", new Object[] { this.type.getDescription() }));
/*  98 */       this.linesCache.add(Component.literal(this.uuid.toString()));
/*     */     } 
/* 100 */     return this.linesCache;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean equals(Object o) {
/* 105 */     if (this == o) {
/* 106 */       return true;
/*     */     }
/* 108 */     if (o == null || getClass() != o.getClass()) {
/* 109 */       return false;
/*     */     }
/*     */     
/* 112 */     EntityTooltipInfo that = (EntityTooltipInfo)o;
/* 113 */     return (this.type.equals(that.type) && this.uuid.equals(that.uuid) && this.name.equals(that.name));
/*     */   }
/*     */ 
/*     */   
/*     */   public int hashCode() {
/* 118 */     result = this.type.hashCode();
/* 119 */     result = 31 * result + this.uuid.hashCode();
/* 120 */     return 31 * result + this.name.hashCode();
/*     */   }
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\network\chat\HoverEvent$EntityTooltipInfo.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */