/*    */ package net.minecraft.world.item.component;
/*    */ 
/*    */ import com.mojang.serialization.Codec;
/*    */ import io.netty.buffer.ByteBuf;
/*    */ import java.util.function.Consumer;
/*    */ import java.util.function.Function;
/*    */ import net.minecraft.core.component.DataComponentType;
/*    */ import net.minecraft.nbt.CompoundTag;
/*    */ import net.minecraft.nbt.NbtUtils;
/*    */ import net.minecraft.nbt.TagParser;
/*    */ import net.minecraft.network.codec.ByteBufCodecs;
/*    */ import net.minecraft.network.codec.StreamCodec;
/*    */ import net.minecraft.world.item.ItemStack;
/*    */ 
/*    */ public final class CustomData {
/* 16 */   public static final CustomData EMPTY = new CustomData(new CompoundTag());
/*    */   
/* 18 */   public static final Codec<CompoundTag> COMPOUND_TAG_CODEC = Codec.withAlternative(CompoundTag.CODEC, TagParser.FLATTENED_CODEC);
/* 19 */   public static final Codec<CustomData> CODEC = COMPOUND_TAG_CODEC.xmap(CustomData::new, data -> data.tag);
/*    */ 
/*    */   
/*    */   @Deprecated
/* 23 */   public static final StreamCodec<ByteBuf, CustomData> STREAM_CODEC = ByteBufCodecs.COMPOUND_TAG.map(CustomData::new, data -> data.tag);
/*    */ 
/*    */   
/*    */   private final CompoundTag tag;
/*    */ 
/*    */   
/* 29 */   private CustomData(CompoundTag tag) { this.tag = tag; }
/*    */ 
/*    */ 
/*    */   
/* 33 */   public static CustomData of(CompoundTag tag) { return new CustomData(tag.copy()); }
/*    */ 
/*    */ 
/*    */   
/* 37 */   public boolean matchedBy(CompoundTag expectedTag) { return NbtUtils.compareNbt(expectedTag, this.tag, true); }
/*    */ 
/*    */   
/*    */   public static void update(DataComponentType<CustomData> component, ItemStack itemStack, Consumer<CompoundTag> consumer) {
/* 41 */     CustomData newData = ((CustomData)itemStack.getOrDefault(component, EMPTY)).update(consumer);
/* 42 */     if (newData.tag.isEmpty()) {
/* 43 */       itemStack.remove(component);
/*    */     } else {
/* 45 */       itemStack.set(component, newData);
/*    */     } 
/*    */   }
/*    */   
/*    */   public static void set(DataComponentType<CustomData> component, ItemStack itemStack, CompoundTag tag) {
/* 50 */     if (!tag.isEmpty()) {
/* 51 */       itemStack.set(component, of(tag));
/*    */     } else {
/* 53 */       itemStack.remove(component);
/*    */     } 
/*    */   }
/*    */   
/*    */   public CustomData update(Consumer<CompoundTag> consumer) {
/* 58 */     CompoundTag newTag = this.tag.copy();
/* 59 */     consumer.accept(newTag);
/* 60 */     return new CustomData(newTag);
/*    */   }
/*    */ 
/*    */   
/* 64 */   public boolean isEmpty() { return this.tag.isEmpty(); }
/*    */ 
/*    */ 
/*    */   
/* 68 */   public CompoundTag copyTag() { return this.tag.copy(); }
/*    */ 
/*    */ 
/*    */   
/*    */   public boolean equals(Object obj) {
/* 73 */     if (obj == this) {
/* 74 */       return true;
/*    */     }
/* 76 */     if (obj instanceof CustomData) { CustomData customData = (CustomData)obj;
/* 77 */       return this.tag.equals(customData.tag); }
/*    */     
/* 79 */     return false;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/* 84 */   public int hashCode() { return this.tag.hashCode(); }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 89 */   public String toString() { return this.tag.toString(); }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\item\component\CustomData.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */