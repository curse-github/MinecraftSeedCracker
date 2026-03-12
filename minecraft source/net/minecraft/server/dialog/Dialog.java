/*    */ package net.minecraft.server.dialog;
/*    */ 
/*    */ import com.mojang.serialization.Codec;
/*    */ import com.mojang.serialization.MapCodec;
/*    */ import io.netty.buffer.ByteBuf;
/*    */ import java.util.Optional;
/*    */ import net.minecraft.core.Holder;
/*    */ import net.minecraft.core.HolderSet;
/*    */ import net.minecraft.core.RegistryCodecs;
/*    */ import net.minecraft.core.registries.BuiltInRegistries;
/*    */ import net.minecraft.core.registries.Registries;
/*    */ import net.minecraft.network.RegistryFriendlyByteBuf;
/*    */ import net.minecraft.network.codec.ByteBufCodecs;
/*    */ import net.minecraft.network.codec.StreamCodec;
/*    */ import net.minecraft.resources.RegistryFileCodec;
/*    */ import net.minecraft.server.dialog.action.Action;
/*    */ import net.minecraft.util.ExtraCodecs;
/*    */ 
/*    */ 
/*    */ public interface Dialog
/*    */ {
/* 22 */   public static final Codec<Integer> WIDTH_CODEC = ExtraCodecs.intRange(1, 1024);
/*    */   
/* 24 */   public static final Codec<Dialog> DIRECT_CODEC = BuiltInRegistries.DIALOG_TYPE.byNameCodec().dispatch(Dialog::codec, c -> c);
/*    */   CommonDialogData common();
/* 26 */   public static final Codec<Holder<Dialog>> CODEC = RegistryFileCodec.create(Registries.DIALOG, DIRECT_CODEC); MapCodec<? extends Dialog> codec();
/*    */   Optional<Action> onCancel();
/* 28 */   public static final Codec<HolderSet<Dialog>> LIST_CODEC = RegistryCodecs.homogeneousList(Registries.DIALOG, DIRECT_CODEC);
/*    */ 
/*    */   
/* 31 */   public static final StreamCodec<RegistryFriendlyByteBuf, Holder<Dialog>> STREAM_CODEC = ByteBufCodecs.holder(Registries.DIALOG, ByteBufCodecs.fromCodecWithRegistriesTrusted(DIRECT_CODEC));
/*    */   
/* 33 */   public static final StreamCodec<ByteBuf, Dialog> CONTEXT_FREE_STREAM_CODEC = ByteBufCodecs.fromCodecTrusted(DIRECT_CODEC);
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\server\dialog\Dialog.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */