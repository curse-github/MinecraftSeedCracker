/*    */ package net.minecraft.world.level.storage.loot;
/*    */ 
/*    */ import com.mojang.serialization.Codec;
/*    */ import java.util.function.Function;
/*    */ import net.minecraft.util.ExtraCodecs;
/*    */ import net.minecraft.util.StringRepresentable;
/*    */ import net.minecraft.util.context.ContextKey;
/*    */ import net.minecraft.world.entity.Entity;
/*    */ import net.minecraft.world.item.ItemStack;
/*    */ import net.minecraft.world.level.block.entity.BlockEntity;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public final class ArgCodecBuilder<R>
/*    */   extends Object
/*    */ {
/* 59 */   private final ExtraCodecs.LateBoundIdMapper<String, LootContextArg<R>> sources = new ExtraCodecs.LateBoundIdMapper();
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public <T> ArgCodecBuilder<R> anyOf(T[] targets, Function<T, String> nameGetter, Function<T, ? extends LootContextArg<R>> argFactory) {
/* 65 */     for (T target : targets) {
/* 66 */       this.sources.put((String)nameGetter.apply(target), (LootContextArg)argFactory.apply(target));
/*    */     }
/* 68 */     return this;
/*    */   }
/*    */ 
/*    */   
/* 72 */   public <T extends StringRepresentable> ArgCodecBuilder<R> anyOf(T[] targets, Function<T, ? extends LootContextArg<R>> argFactory) { return anyOf(targets, StringRepresentable::getSerializedName, argFactory); }
/*    */ 
/*    */ 
/*    */   
/* 76 */   public <T extends StringRepresentable & LootContextArg<? extends R>> ArgCodecBuilder<R> anyOf(T[] targets) { return anyOf(targets, x$0 -> LootContextArg.cast((LootContextArg)x$0)); }
/*    */ 
/*    */ 
/*    */   
/* 80 */   public ArgCodecBuilder<R> anyEntity(Function<? super ContextKey<? extends Entity>, ? extends LootContextArg<R>> function) { return anyOf(LootContext.EntityTarget.values(), target -> (LootContextArg)function.apply(target.contextParam())); }
/*    */ 
/*    */ 
/*    */   
/* 84 */   public ArgCodecBuilder<R> anyBlockEntity(Function<? super ContextKey<? extends BlockEntity>, ? extends LootContextArg<R>> function) { return anyOf(LootContext.BlockEntityTarget.values(), target -> (LootContextArg)function.apply(target.contextParam())); }
/*    */ 
/*    */ 
/*    */   
/* 88 */   public ArgCodecBuilder<R> anyItemStack(Function<? super ContextKey<? extends ItemStack>, ? extends LootContextArg<R>> function) { return anyOf(LootContext.ItemStackTarget.values(), target -> (LootContextArg)function.apply(target.contextParam())); }
/*    */ 
/*    */ 
/*    */   
/* 92 */   private Codec<LootContextArg<R>> build() { return this.sources.codec(Codec.STRING); }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\level\storage\loot\LootContextArg$ArgCodecBuilder.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */