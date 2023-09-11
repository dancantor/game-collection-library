import { NgModule } from '@angular/core';
import { PreloadAllModules, RouterModule, Routes } from '@angular/router';

const routes: Routes = [
  {
    path: 'game-library-list',
    loadChildren: () => import('./pages/game-library-list/game-library-list.module').then( m => m.GameLibraryListPageModule)
  },
  {
    path: 'game-details/:id',
    loadChildren: () => import('./pages/game-details/game-details.module').then( m => m.GameDetailsPageModule)
  },
  {
    path: '',
    redirectTo: 'game-library-list',
    pathMatch: 'full'
  },
  {
    path: 'add-update-game',
    loadChildren: () => import('./pages/add-update-game/add-update-game.module').then( m => m.AddUpdateGamePageModule)
  },
];

@NgModule({
  imports: [
    RouterModule.forRoot(routes, { preloadingStrategy: PreloadAllModules })
  ],
  exports: [RouterModule]
})
export class AppRoutingModule { }
