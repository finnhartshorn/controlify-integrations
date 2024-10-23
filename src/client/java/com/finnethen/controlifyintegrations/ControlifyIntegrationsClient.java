package com.finnethen.controlifyintegrations;

import net.fabricmc.api.ClientModInitializer;

public class ControlifyIntegrationsClient implements ClientModInitializer {
	@Override
	public void onInitializeClient() {
		// This entrypoint is suitable for setting up client-specific logic, such as rendering.
	}
}


//public static InputBindingSupplier MY_CUSTOM_BINDING = ControlifyBindingApi.get().registerBinding(builder ->
//		.id("my_mod", "my_custom_binding")
//		.category(Component.literal("Cool category"))
//		.allowedContexts(BindContexts.IN_GAME) // highly recommended - will suppress outputs like justPressed() if incorrect context
//		.keyEmulation(options.keyJump) // example, optional
//		.addKeyCorrelation(options.keyAttack) // example, optional
//		.radialCandidate(RadialIcons.getEffect(MobEffect.JUMP_BOOST))); // example, optional