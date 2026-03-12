/*    */ package net.minecraft.core.particles;
/*    */ import com.mojang.serialization.Codec;
/*    */ import com.mojang.serialization.MapCodec;
/*    */ import java.util.function.Function;
/*    */ import net.minecraft.network.RegistryFriendlyByteBuf;
/*    */ import net.minecraft.network.codec.StreamCodec;
/*    */ import net.minecraft.world.item.Item;
/*    */ import net.minecraft.world.item.ItemStack;
/*    */ 
/*    */ public class ItemParticleOption implements ParticleOptions {
/* 11 */   private static final Codec<ItemStack> ITEM_CODEC = Codec.withAlternative(ItemStack.SINGLE_ITEM_CODEC, Item.CODEC, ItemStack::new);
/*    */   
/*    */   private final ParticleType<ItemParticleOption> type;
/*    */   
/*    */   private final ItemStack itemStack;
/*    */   
/* 17 */   public static MapCodec<ItemParticleOption> codec(ParticleType<ItemParticleOption> type) { return ITEM_CODEC.xmap(stack -> new ItemParticleOption(type, stack), o -> o.itemStack).fieldOf("item"); }
/*    */ 
/*    */ 
/*    */   
/* 21 */   public static StreamCodec<? super RegistryFriendlyByteBuf, ItemParticleOption> streamCodec(ParticleType<ItemParticleOption> type) { return ItemStack.STREAM_CODEC.map(stack -> new ItemParticleOption(type, stack), o -> o.itemStack); }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public ItemParticleOption(ParticleType<ItemParticleOption> type, ItemStack itemStack) {
/* 28 */     if (itemStack.isEmpty()) {
/* 29 */       throw new IllegalArgumentException("Empty stacks are not allowed");
/*    */     }
/* 31 */     this.type = type;
/* 32 */     this.itemStack = itemStack;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/* 37 */   public ParticleType<ItemParticleOption> getType() { return this.type; }
/*    */ 
/*    */ 
/*    */   
/* 41 */   public ItemStack getItem() { return this.itemStack; }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\core\particles\ItemParticleOption.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */