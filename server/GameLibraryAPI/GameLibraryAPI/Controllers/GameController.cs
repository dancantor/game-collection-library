using GameLibraryAPI.Dto;
using GameLibraryAPI.Service;
using GameLibraryAPI.Validation;
using Microsoft.AspNetCore.Mvc;

namespace GameLibraryAPI.Controllers
{
    [ApiController]
    [Route("api/[controller]")]
    public class GameController : ControllerBase
    {

        private readonly IGameService gameService;

        public GameController(IGameService gameService)
        {
            this.gameService = gameService;
        }

        [HttpGet("")]
        public async Task<IActionResult> GetAllGames()
        {
            return Ok(await gameService.GetAllGames());
        }

        [HttpGet("{gameId}")]
        public async Task<IActionResult> GetGameById(int gameId)
        {
            try
            {
                var game = await gameService.GetGameById(gameId);
                return Ok(game);
            }
            catch (GameNotFoundException gnfe)
            {
                return NotFound(gnfe.Message);
            }
        }

        [HttpPost("")]
        public async Task<IActionResult> AddNewGame([FromBody] GameDto game)
        {
            return Ok(await gameService.AddNewGame(game));
        }

        [HttpDelete("{gameId}")]
        public async Task<IActionResult> RemoveGame(int gameId)
        {
            try
            {
                var game = await gameService.RemoveGame(gameId);
                return Ok(game);
            }
            catch (GameNotFoundException gnfe)
            {
                return NotFound(gnfe.Message);
            }
        }

        [HttpPut("{gameId}")]
        public async Task<IActionResult> UpdateGame([FromBody] GameDto gameDto, int gameId)
        {
            try
            {
                var game = await gameService.UpdateGame(gameDto, gameId);
                return Ok(game);
            }
            catch (GameNotFoundException gnfe)
            {
                return NotFound(gnfe.Message);
            }

        }


    }
}