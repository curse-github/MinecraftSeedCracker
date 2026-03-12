/*    */ package net.minecraft.world.waypoints;
/*    */ 
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.serialization.Codec;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import io.netty.buffer.ByteBuf;
/*    */ import java.util.Optional;
/*    */ import java.util.function.BiFunction;
/*    */ import net.minecraft.core.component.DataComponents;
/*    */ import net.minecraft.network.codec.ByteBufCodecs;
/*    */ import net.minecraft.network.codec.StreamCodec;
/*    */ import net.minecraft.resources.Identifier;
/*    */ import net.minecraft.resources.ResourceKey;
/*    */ import net.minecraft.util.ExtraCodecs;
/*    */ import net.minecraft.world.entity.EquipmentSlotGroup;
/*    */ import net.minecraft.world.entity.LivingEntity;
/*    */ import net.minecraft.world.entity.ai.attributes.AttributeModifier;
/*    */ import net.minecraft.world.entity.ai.attributes.Attributes;
/*    */ import net.minecraft.world.item.Item;
/*    */ import net.minecraft.world.item.component.ItemAttributeModifiers;
/*    */ import net.minecraft.world.scores.PlayerTeam;
/*    */ 
/*    */ public interface Waypoint {
/* 24 */   public static final AttributeModifier WAYPOINT_TRANSMIT_RANGE_HIDE_MODIFIER = new AttributeModifier(
/* 25 */       Identifier.withDefaultNamespace("waypoint_transmit_range_hide"), -1.0D, AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL);
/*    */ 
/*    */   
/*    */   public static final int MAX_RANGE = 60000000;
/*    */ 
/*    */   
/* 31 */   static Item.Properties addHideAttribute(Item.Properties properties) { return properties.component(DataComponents.ATTRIBUTE_MODIFIERS, ItemAttributeModifiers.builder().add(Attributes.WAYPOINT_TRANSMIT_RANGE, WAYPOINT_TRANSMIT_RANGE_HIDE_MODIFIER, EquipmentSlotGroup.HEAD, ItemAttributeModifiers.Display.hidden()).build()); }
/*    */   
/*    */   public static class Icon
/*    */   {
/* 35 */     public static final Codec<Icon> CODEC = RecordCodecBuilder.create(i -> i.group(
/* 36 */           ResourceKey.codec(WaypointStyleAssets.ROOT_ID).fieldOf("style").forGetter(()), ExtraCodecs.RGB_COLOR_CODEC
/* 37 */           .optionalFieldOf("color").forGetter(()))
/* 38 */         .apply(i, Icon::new));
/* 39 */     public static final StreamCodec<ByteBuf, Icon> STREAM_CODEC = StreamCodec.composite(
/* 40 */         ResourceKey.streamCodec(WaypointStyleAssets.ROOT_ID), icon -> icon.style, 
/* 41 */         ByteBufCodecs.optional(ByteBufCodecs.RGB_COLOR), icon -> icon.color, Icon::new);
/*    */ 
/*    */ 
/*    */     
/* 45 */     public static final Icon NULL = new Icon();
/*    */     
/* 47 */     public ResourceKey<WaypointStyleAsset> style = WaypointStyleAssets.DEFAULT;
/* 48 */     public Optional<Integer> color = Optional.empty();
/*    */ 
/*    */ 
/*    */ 
/*    */     
/*    */     private Icon(ResourceKey<WaypointStyleAsset> style, Optional<Integer> color) {
/* 54 */       this.style = style;
/* 55 */       this.color = color;
/*    */     }
/*    */ 
/*    */     
/* 59 */     public boolean hasData() { return (this.style != WaypointStyleAssets.DEFAULT || this.color.isPresent()); }
/*    */ 
/*    */     
/*    */     public Icon cloneAndAssignStyle(LivingEntity livingEntity) {
/* 63 */       ResourceKey<WaypointStyleAsset> overrideStyle = getOverrideStyle();
/*    */       
/* 65 */       Optional<Integer> colorOverride = this.color.or(() -> 
/* 66 */           Optional.ofNullable(livingEntity.getTeam())
/* 67 */           .map(())
/* 68 */           .map(()));
/*    */ 
/*    */       
/* 71 */       if (overrideStyle == this.style && colorOverride.isEmpty()) {
/* 72 */         return this;
/*    */       }
/*    */       
/* 75 */       return new Icon(overrideStyle, colorOverride);
/*    */     }
/*    */     
/*    */     public void copyFrom(Icon other) {
/* 79 */       this.color = other.color;
/* 80 */       this.style = other.style;
/*    */     }
/*    */ 
/*    */     
/* 84 */     private ResourceKey<WaypointStyleAsset> getOverrideStyle() { return (this.style != WaypointStyleAssets.DEFAULT) ? this.style : WaypointStyleAssets.DEFAULT; }
/*    */     
/*    */     public Icon() {}
/*    */   }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\waypoints\Waypoint.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */