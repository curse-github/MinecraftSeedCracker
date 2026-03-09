/*     */ package net.minecraft.network.chat;
/*     */ import com.mojang.datafixers.kinds.App;
/*     */ import com.mojang.datafixers.util.Function3;
/*     */ import com.mojang.serialization.Codec;
/*     */ import com.mojang.serialization.DataResult;
/*     */ import com.mojang.serialization.Lifecycle;
/*     */ import com.mojang.serialization.MapCodec;
/*     */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
/*     */ import java.util.Optional;
/*     */ import java.util.UUID;
/*     */ import java.util.function.Function;
/*     */ import net.minecraft.core.UUIDUtil;
/*     */ import net.minecraft.core.registries.BuiltInRegistries;
/*     */ import net.minecraft.util.StringRepresentable;
/*     */ import net.minecraft.world.entity.EntityType;
/*     */ import net.minecraft.world.item.ItemStack;
/*     */ 
/*     */ public interface HoverEvent {
/*  21 */   public static final Codec<HoverEvent> CODEC = Action.CODEC.dispatch("action", HoverEvent::action, action -> action.codec);
/*     */   Action action();
/*     */   public static final class ShowText extends Record implements HoverEvent { private final Component value;
/*     */     
/*  25 */     public ShowText(Component value) { this.value = value; } public final String toString() { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> toString : (Lnet/minecraft/network/chat/HoverEvent$ShowText;)Ljava/lang/String;
/*     */       //   6: areturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #25	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*  25 */       //   0	7	0	this	Lnet/minecraft/network/chat/HoverEvent$ShowText; } public Component value() { return this.value; } public final int hashCode() { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/network/chat/HoverEvent$ShowText;)I
/*     */       //   6: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #25	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/network/chat/HoverEvent$ShowText; } public final boolean equals(Object o) { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: aload_1
/*     */       //   2: <illegal opcode> equals : (Lnet/minecraft/network/chat/HoverEvent$ShowText;Ljava/lang/Object;)Z
/*     */       //   7: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #25	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	8	0	this	Lnet/minecraft/network/chat/HoverEvent$ShowText;
/*     */       //   0	8	1	o	Ljava/lang/Object; }
/*  26 */     public static final MapCodec<ShowText> CODEC = RecordCodecBuilder.mapCodec(i -> i.group(ComponentSerialization.CODEC
/*  27 */           .fieldOf("value").forGetter(ShowText::value))
/*  28 */         .apply(i, ShowText::new));
/*     */ 
/*     */ 
/*     */     
/*  32 */     public HoverEvent.Action action() { return HoverEvent.Action.SHOW_TEXT; } }
/*     */   public static final class ShowItem extends Record implements HoverEvent { private final ItemStack item;
/*     */     public final String toString() { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> toString : (Lnet/minecraft/network/chat/HoverEvent$ShowItem;)Ljava/lang/String;
/*     */       //   6: areturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #36	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/network/chat/HoverEvent$ShowItem; }
/*     */     
/*  36 */     public ItemStack item() { return this.item; }
/*  37 */     public static final MapCodec<ShowItem> CODEC = ItemStack.MAP_CODEC.xmap(ShowItem::new, ShowItem::item);
/*     */     
/*     */     public ShowItem(ItemStack item) {
/*  40 */       item = item.copy();
/*     */       this.item = item;
/*     */     }
/*     */ 
/*     */     
/*  45 */     public HoverEvent.Action action() { return HoverEvent.Action.SHOW_ITEM; }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*  50 */     public boolean equals(Object obj) { if (obj instanceof ShowItem) { ShowItem showItem = (ShowItem)obj; if (ItemStack.matches(this.item, showItem.item)); }  return false; }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*  55 */     public int hashCode() { return ItemStack.hashItemAndComponents(this.item); } }
/*     */   
/*     */   public static final class ShowEntity extends Record implements HoverEvent { private final HoverEvent.EntityTooltipInfo entity;
/*     */     
/*  59 */     public ShowEntity(HoverEvent.EntityTooltipInfo entity) { this.entity = entity; } public final String toString() { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> toString : (Lnet/minecraft/network/chat/HoverEvent$ShowEntity;)Ljava/lang/String;
/*     */       //   6: areturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #59	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/network/chat/HoverEvent$ShowEntity; } public final int hashCode() { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/network/chat/HoverEvent$ShowEntity;)I
/*     */       //   6: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #59	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/network/chat/HoverEvent$ShowEntity; } public final boolean equals(Object o) { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: aload_1
/*     */       //   2: <illegal opcode> equals : (Lnet/minecraft/network/chat/HoverEvent$ShowEntity;Ljava/lang/Object;)Z
/*     */       //   7: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #59	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	8	0	this	Lnet/minecraft/network/chat/HoverEvent$ShowEntity;
/*  59 */       //   0	8	1	o	Ljava/lang/Object; } public HoverEvent.EntityTooltipInfo entity() { return this.entity; }
/*  60 */     public static final MapCodec<ShowEntity> CODEC = RecordCodecBuilder.mapCodec(i -> i.group(HoverEvent.EntityTooltipInfo.CODEC
/*  61 */           .forGetter(ShowEntity::entity))
/*  62 */         .apply(i, ShowEntity::new));
/*     */ 
/*     */ 
/*     */     
/*  66 */     public HoverEvent.Action action() { return HoverEvent.Action.SHOW_ENTITY; } }
/*     */ 
/*     */   
/*     */   public static class EntityTooltipInfo
/*     */   {
/*  71 */     public static final MapCodec<EntityTooltipInfo> CODEC = RecordCodecBuilder.mapCodec(i -> i.group(BuiltInRegistries.ENTITY_TYPE
/*  72 */           .byNameCodec().fieldOf("id").forGetter(()), UUIDUtil.LENIENT_CODEC
/*  73 */           .fieldOf("uuid").forGetter(()), ComponentSerialization.CODEC
/*  74 */           .optionalFieldOf("name").forGetter(()))
/*  75 */         .apply(i, EntityTooltipInfo::new));
/*     */     
/*     */     public final EntityType<?> type;
/*     */     
/*     */     public final UUID uuid;
/*     */     
/*     */     public final Optional<Component> name;
/*     */     private List<Component> linesCache;
/*     */     
/*  84 */     public EntityTooltipInfo(EntityType<?> type, UUID uuid, Component name) { this(type, uuid, Optional.ofNullable(name)); }
/*     */ 
/*     */     
/*     */     public EntityTooltipInfo(EntityType<?> type, UUID uuid, Optional<Component> name) {
/*  88 */       this.type = type;
/*  89 */       this.uuid = uuid;
/*  90 */       this.name = name;
/*     */     }
/*     */     
/*     */     public List<Component> getTooltipLines() {
/*  94 */       if (this.linesCache == null) {
/*  95 */         this.linesCache = new ArrayList();
/*  96 */         Objects.requireNonNull(this.linesCache); this.name.ifPresent(this.linesCache::add);
/*  97 */         this.linesCache.add(Component.translatable("gui.entity_tooltip.type", new Object[] { this.type.getDescription() }));
/*  98 */         this.linesCache.add(Component.literal(this.uuid.toString()));
/*     */       } 
/* 100 */       return this.linesCache;
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean equals(Object o) {
/* 105 */       if (this == o) {
/* 106 */         return true;
/*     */       }
/* 108 */       if (o == null || getClass() != o.getClass()) {
/* 109 */         return false;
/*     */       }
/*     */       
/* 112 */       EntityTooltipInfo that = (EntityTooltipInfo)o;
/* 113 */       return (this.type.equals(that.type) && this.uuid.equals(that.uuid) && this.name.equals(that.name));
/*     */     }
/*     */ 
/*     */     
/*     */     public int hashCode() {
/* 118 */       result = this.type.hashCode();
/* 119 */       result = 31 * result + this.uuid.hashCode();
/* 120 */       return 31 * result + this.name.hashCode();
/*     */     }
/*     */   }
/*     */   
/*     */   public enum Action
/*     */     implements StringRepresentable
/*     */   {
/* 127 */     SHOW_TEXT("show_text", true, HoverEvent.ShowText.CODEC),
/* 128 */     SHOW_ITEM("show_item", true, HoverEvent.ShowItem.CODEC),
/* 129 */     SHOW_ENTITY("show_entity", true, HoverEvent.ShowEntity.CODEC); public static final Codec<Action> UNSAFE_CODEC; public static final Codec<Action> CODEC; private final String name; private final boolean allowFromServer; private final MapCodec<? extends HoverEvent> codec;
/*     */     
/*     */     static  {
/* 132 */       UNSAFE_CODEC = StringRepresentable.fromValues(Action::values);
/* 133 */       CODEC = UNSAFE_CODEC.validate(Action::filterForSerialization);
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     Action(String name, boolean allowFromServer, MapCodec<? extends HoverEvent> codec) {
/* 140 */       this.name = name;
/* 141 */       this.allowFromServer = allowFromServer;
/* 142 */       this.codec = codec;
/*     */     }
/*     */ 
/*     */     
/* 146 */     public boolean isAllowedFromServer() { return this.allowFromServer; }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 151 */     public String getSerializedName() { return this.name; }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 156 */     public String toString() { return "<action " + this.name + ">"; }
/*     */ 
/*     */     
/*     */     private static DataResult<Action> filterForSerialization(Action action) {
/* 160 */       if (!action.isAllowedFromServer()) {
/* 161 */         return DataResult.error(() -> "Action not allowed: " + String.valueOf(action));
/*     */       }
/* 163 */       return DataResult.success(action, Lifecycle.stable());
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\network\chat\HoverEvent.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */