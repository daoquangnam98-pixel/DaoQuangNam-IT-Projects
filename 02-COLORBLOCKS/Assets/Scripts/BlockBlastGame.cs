
using System.Collections.Generic;
using UnityEngine;
using UnityEngine.InputSystem;

public class BlockBlastGame : MonoBehaviour
{
    private const int SIZE = 8
 ;
    private const float BOARD_CENTER_Y = 0.55f;
    private const float TRAY_Y = -3.0f;

    public float cellSize = 0.34f;
    public float spacing = 0.01f;

    private float step;
    private float boardBottom;
    private float boardLeft;

    private bool[,] occupied = new bool[SIZE, SIZE];
    private GameObject[,] placedBlocks = new GameObject[SIZE, SIZE];

    private Transform boardRoot;
    private Sprite squareSprite;

    private int score;
    private int highScore;
    private bool gameOver;

    private readonly List<GameObject> currentPieces =
        new List<GameObject>();

    private readonly List<GameObject> previewBlocks =
        new List<GameObject>();

    private readonly Color[] colors =
    {
        new Color(1f, 0.25f, 0.35f),
        new Color(0.15f, 0.85f, 1f),
        new Color(1f, 0.75f, 0.15f),
        new Color(0.55f, 0.35f, 1f),
        new Color(0.25f, 1f, 0.55f),
        new Color(1f, 0.35f, 0.85f)
    };

    private GUIStyle titleStyle;
    private GUIStyle headingStyle;
    private GUIStyle scoreStyle;
    private GUIStyle hintStyle;
    private GUIStyle gameOverStyle;
    private GUIStyle buttonStyle;

    public bool IsGameOver => gameOver;

    void Start()
    {
        Application.targetFrameRate = 60;

        step = cellSize + spacing;
        highScore = PlayerPrefs.GetInt("BlockBlastHighScore", 0);

        Camera cam = Camera.main;

        if (cam == null)
        {
            Debug.LogError("Khong tim thay Main Camera!");
            enabled = false;
            return;
        }

        cam.transform.position = new Vector3(0f, 0f, -10f);
        cam.orthographic = true;

        // Dieu chinh camera theo man hinh doc dien thoai.
        cam.orthographicSize = Mathf.Max(
            4.6f,
            2.15f / cam.aspect
        );

        cam.backgroundColor = new Color(0.07f, 0.08f, 0.16f);

        Texture2D texture = new Texture2D(1, 1);
        texture.SetPixel(0, 0, Color.white);
        texture.Apply();

        squareSprite = Sprite.Create(
            texture,
            new Rect(0, 0, 1, 1),
            new Vector2(0.5f, 0.5f),
            1f
        );

        boardLeft = -((SIZE - 1) * step) / 2f;
        boardBottom = BOARD_CENTER_Y - ((SIZE - 1) * step) / 2f;

        CreateBoard();
        CreatePieces();
    }

    GameObject CreateSquare(
        string objectName,
        Vector2 position,
        float size,
        Color color,
        int sortingOrder)
    {
        GameObject obj = new GameObject(objectName);
        obj.transform.position = new Vector3(
            position.x, position.y, 0f
        );

        obj.transform.localScale = new Vector3(size, size, 1f);

        SpriteRenderer sr = obj.AddComponent<SpriteRenderer>();
        sr.sprite = squareSprite;
        sr.color = color;
        sr.sortingOrder = sortingOrder;

        return obj;
    }

    void CreateBoard()
    {
        boardRoot = new GameObject("GameBoard").transform;

        for (int row = 0; row < SIZE; row++)
        {
            for (int col = 0; col < SIZE; col++)
            {
                GameObject cell = CreateSquare(
                    "Cell_" + row + "_" + col,
                    new Vector2(
                        boardLeft + col * step,
                        boardBottom + row * step
                    ),
                    cellSize,
                    new Color(0.16f, 0.19f, 0.32f),
                    0
                );

                cell.transform.SetParent(boardRoot, true);
            }
        }
    }

    List<Vector2Int> RandomShape()
    {
        List<Vector2Int>[] shapes =
        {
            new List<Vector2Int>
            {
                new Vector2Int(0, 0)
            },
            new List<Vector2Int>
            {
                new Vector2Int(0, 0),
                new Vector2Int(1, 0)
            },
            new List<Vector2Int>
            {
                new Vector2Int(0, 0),
                new Vector2Int(1, 0),
                new Vector2Int(2, 0)
            },
            new List<Vector2Int>
            {
                new Vector2Int(0, 0),
                new Vector2Int(1, 0),
                new Vector2Int(0, 1),
                new Vector2Int(1, 1)
            },
            new List<Vector2Int>
            {
                new Vector2Int(0, 0),
                new Vector2Int(0, 1),
                new Vector2Int(0, 2)
            },
            new List<Vector2Int>
            {
                new Vector2Int(0, 0),
                new Vector2Int(1, 0),
                new Vector2Int(2, 0),
                new Vector2Int(1, 1)
            },
            new List<Vector2Int>
            {
                new Vector2Int(0, 0),
                new Vector2Int(0, 1),
                new Vector2Int(1, 1)
            },
            new List<Vector2Int>
            {
                new Vector2Int(0, 0),
                new Vector2Int(1, 0),
                new Vector2Int(2, 0),
                new Vector2Int(3, 0)
            }
        };

        return new List<Vector2Int>(
            shapes[Random.Range(0, shapes.Length)]
        );
    }

    void CreatePieces()
    {
        ClearPlacementPreview();

        foreach (GameObject oldPiece in currentPieces)
        {
            if (oldPiece != null)
                Destroy(oldPiece);
        }

        currentPieces.Clear();

        // Ba khoi duoc dat gon trong man hinh dien thoai.
        Vector2[] positions =
        {
            new Vector2(-1.25f, TRAY_Y),
            new Vector2(0f, TRAY_Y),
            new Vector2(1.25f, TRAY_Y)
        };

        for (int i = 0; i < positions.Length; i++)
        {
            List<Vector2Int> shape = RandomShape();
            Color pieceColor = colors[i];

            int minX = 99, maxX = -99;
            int minY = 99, maxY = -99;

            foreach (Vector2Int p in shape)
            {
                minX = Mathf.Min(minX, p.x);
                maxX = Mathf.Max(maxX, p.x);
                minY = Mathf.Min(minY, p.y);
                maxY = Mathf.Max(maxY, p.y);
            }

            GameObject root = new GameObject("DraggableBlock");
            root.transform.position = positions[i];

            BlockPiece drag = root.AddComponent<BlockPiece>();
            drag.game = this;

            foreach (Vector2Int p in shape)
            {
                Vector2 local = new Vector2(
                    (p.x - (minX + maxX) / 2f) * step,
                    (p.y - (minY + maxY) / 2f) * step
                );

                GameObject block = CreateSquare(
                    "PieceCell",
                    (Vector2)root.transform.position + local,
                    cellSize * 0.99f,
                    pieceColor,
                    3
                );

                block.transform.SetParent(root.transform, true);
            }

            BoxCollider2D collider =
                root.AddComponent<BoxCollider2D>();

            collider.size = new Vector2(
                (maxX - minX + 1) * step,
                (maxY - minY + 1) * step
            );

            currentPieces.Add(root);
        }

        CheckGameOver();
    }

    public void UpdatePlacementPreview(GameObject piece)
    {
        if (piece == null || gameOver)
        {
            ClearPlacementPreview();
            return;
        }

        SpriteRenderer[] renderers =
            piece.GetComponentsInChildren<SpriteRenderer>();

        List<SpriteRenderer> cells = new List<SpriteRenderer>();

        foreach (SpriteRenderer sr in renderers)
        {
            if (sr.gameObject != piece)
                cells.Add(sr);
        }

        while (previewBlocks.Count < cells.Count)
        {
            GameObject preview = CreateSquare(
                "PlacementPreview",
                Vector2.zero,
                cellSize * 0.96f,
                Color.white,
                5
            );

            previewBlocks.Add(preview);
        }

        bool valid = true;
        HashSet<Vector2Int> targetCells =
            new HashSet<Vector2Int>();

        foreach (SpriteRenderer sr in cells)
        {
            Vector3 pos = sr.transform.position;

            int col = Mathf.RoundToInt(
                (pos.x - boardLeft) / step
            );

            int row = Mathf.RoundToInt(
                (pos.y - boardBottom) / step
            );

            if (col < 0 || col >= SIZE ||
                row < 0 || row >= SIZE)
            {
                valid = false;
                continue;
            }

            float targetX = boardLeft + col * step;
            float targetY = boardBottom + row * step;

            if (Mathf.Abs(pos.x - targetX) > step * 0.35f ||
                Mathf.Abs(pos.y - targetY) > step * 0.35f ||
                occupied[col, row] ||
                !targetCells.Add(new Vector2Int(col, row)))
            {
                valid = false;
            }
        }

        Color previewColor = valid
            ? new Color(0.2f, 1f, 0.45f, 0.58f)
            : new Color(1f, 0.18f, 0.25f, 0.58f);

        for (int i = 0; i < cells.Count; i++)
        {
            Vector3 pos = cells[i].transform.position;

            int col = Mathf.RoundToInt(
                (pos.x - boardLeft) / step
            );

            int row = Mathf.RoundToInt(
                (pos.y - boardBottom) / step
            );

            GameObject preview = previewBlocks[i];
            preview.SetActive(true);

            if (col >= 0 && col < SIZE &&
                row >= 0 && row < SIZE)
            {
                preview.transform.position = new Vector3(
                    boardLeft + col * step,
                    boardBottom + row * step,
                    0f
                );
            }
            else
            {
                preview.transform.position =
                    new Vector3(pos.x, pos.y, 0f);
            }

            preview.transform.localScale = new Vector3(
                cellSize * 0.96f,
                cellSize * 0.96f,
                1f
            );

            SpriteRenderer sr =
                preview.GetComponent<SpriteRenderer>();

            sr.color = previewColor;
            sr.sortingOrder = 5;
        }

        for (int i = cells.Count; i < previewBlocks.Count; i++)
        {
            if (previewBlocks[i] != null)
                previewBlocks[i].SetActive(false);
        }
    }

    public void ClearPlacementPreview()
    {
        foreach (GameObject preview in previewBlocks)
        {
            if (preview != null)
                preview.SetActive(false);
        }
    }

    public void SnapAndPlace(GameObject piece)
    {
        if (piece == null || gameOver)
            return;

        SpriteRenderer[] renderers =
            piece.GetComponentsInChildren<SpriteRenderer>();

        if (renderers.Length == 0)
        {
            ReturnPiece(piece);
            return;
        }

        Vector3 firstPosition = renderers[0].transform.position;

        int col = Mathf.RoundToInt(
            (firstPosition.x - boardLeft) / step
        );

        int row = Mathf.RoundToInt(
            (firstPosition.y - boardBottom) / step
        );

        Vector3 targetPosition = new Vector3(
            boardLeft + col * step,
            boardBottom + row * step,
            0f
        );

        piece.transform.position +=
            targetPosition - firstPosition;

        ClearPlacementPreview();
        TryPlace(piece);
    }

    public void TryPlace(GameObject piece)
    {
        if (piece == null || gameOver)
            return;

        SpriteRenderer[] renderers =
            piece.GetComponentsInChildren<SpriteRenderer>();

        List<Vector2Int> cells = new List<Vector2Int>();
        List<GameObject> blocks = new List<GameObject>();

        HashSet<Vector2Int> uniqueCells =
            new HashSet<Vector2Int>();

        foreach (SpriteRenderer sr in renderers)
        {
            if (sr.gameObject == piece)
                continue;

            Vector3 pos = sr.transform.position;

            int col = Mathf.RoundToInt(
                (pos.x - boardLeft) / step
            );

            int row = Mathf.RoundToInt(
                (pos.y - boardBottom) / step
            );

            if (col < 0 || col >= SIZE ||
                row < 0 || row >= SIZE)
            {
                ReturnPiece(piece);
                return;
            }

            float targetX = boardLeft + col * step;
            float targetY = boardBottom + row * step;

            Vector2Int cell = new Vector2Int(col, row);

            if (Mathf.Abs(pos.x - targetX) > step * 0.35f ||
                Mathf.Abs(pos.y - targetY) > step * 0.35f ||
                occupied[col, row] ||
                !uniqueCells.Add(cell))
            {
                ReturnPiece(piece);
                return;
            }

            cells.Add(cell);
            blocks.Add(sr.gameObject);
        }

        if (cells.Count == 0)
        {
            ReturnPiece(piece);
            return;
        }

        foreach (GameObject block in blocks)
        {
            if (block != null)
            {
                BlockPiece drag =
                    piece.GetComponent<BlockPiece>();

                if (drag != null)
                    drag.MarkPlaced();
            }
        }

        for (int i = 0; i < cells.Count; i++)
        {
            Vector2Int cell = cells[i];
            GameObject block = blocks[i];

            occupied[cell.x, cell.y] = true;
            placedBlocks[cell.x, cell.y] = block;

            block.transform.position = new Vector3(
                boardLeft + cell.x * step,
                boardBottom + cell.y * step,
                0f
            );

            block.transform.SetParent(boardRoot, true);
            block.transform.localScale =
                new Vector3(cellSize, cellSize, 1f);

            block.GetComponent<SpriteRenderer>().sortingOrder = 2;
        }

        currentPieces.Remove(piece);
        Destroy(piece);

        ClearPlacementPreview();

        score += cells.Count;

        ClearCompletedLines();
        UpdateHighScore();

        if (currentPieces.Count == 0)
            CreatePieces();
        else
            CheckGameOver();
    }

    // Neu dat sai, giu nguyen khoi va dua ve vi tri ban dau.
    void ReturnPiece(GameObject piece)
    {
        ClearPlacementPreview();

        if (piece == null)
            return;

        BlockPiece drag = piece.GetComponent<BlockPiece>();

        if (drag != null)
            drag.ReturnToStart();
        else
            piece.transform.position = piece.transform.position;
    }

    void CreateBreakEffect(Vector3 position, Color color)
    {
        for (int i = 0; i < 8; i++)
        {
            GameObject particle = CreateSquare(
                "BreakParticle",
                new Vector2(position.x, position.y),
                0.07f,
                color,
                20
            );

            Rigidbody2D rb = particle.AddComponent<Rigidbody2D>();
            rb.gravityScale = 0f;
            rb.linearDamping = 2f;

            Vector2 direction =
                Random.insideUnitCircle.normalized;

            rb.linearVelocity =
                direction * Random.Range(1.5f, 3.5f);

            Destroy(particle, 0.35f);
        }
    }

    void UpdateHighScore()
    {
        if (score > highScore)
        {
            highScore = score;
            PlayerPrefs.SetInt("BlockBlastHighScore", highScore);
            PlayerPrefs.Save();
        }
    }

    bool CanPieceFit(GameObject piece)
    {
        if (piece == null)
            return false;

        SpriteRenderer[] renderers =
            piece.GetComponentsInChildren<SpriteRenderer>();

        List<Vector2Int> offsets = new List<Vector2Int>();

        foreach (SpriteRenderer sr in renderers)
        {
            if (sr.gameObject == piece)
                continue;

            Vector3 local = sr.transform.localPosition;

            offsets.Add(new Vector2Int(
                Mathf.RoundToInt(local.x / step),
                Mathf.RoundToInt(local.y / step)
            ));
        }

        if (offsets.Count == 0)
            return false;

        for (int row = 0; row < SIZE; row++)
        {
            for (int col = 0; col < SIZE; col++)
            {
                bool fits = true;

                foreach (Vector2Int offset in offsets)
                {
                    int x = col + offset.x;
                    int y = row + offset.y;

                    if (x < 0 || x >= SIZE ||
                        y < 0 || y >= SIZE ||
                        occupied[x, y])
                    {
                        fits = false;
                        break;
                    }
                }

                if (fits)
                    return true;
            }
        }

        return false;
    }

    void CheckGameOver()
    {
        if (gameOver || currentPieces.Count == 0)
            return;

        foreach (GameObject piece in currentPieces)
        {
            if (CanPieceFit(piece))
                return;
        }

        gameOver = true;
        ClearPlacementPreview();

        Debug.Log("GAME OVER!");
    }

    void ClearCompletedLines()
    {
        bool[] fullRows = new bool[SIZE];
        bool[] fullCols = new bool[SIZE];

        int completedLines = 0;

        HashSet<GameObject> blocksToRemove =
            new HashSet<GameObject>();

        for (int row = 0; row < SIZE; row++)
        {
            bool full = true;

            for (int col = 0; col < SIZE; col++)
            {
                if (!occupied[col, row])
                {
                    full = false;
                    break;
                }
            }

            fullRows[row] = full;

            if (full)
            {
                completedLines++;

                for (int col = 0; col < SIZE; col++)
                {
                    if (placedBlocks[col, row] != null)
                        blocksToRemove.Add(placedBlocks[col, row]);
                }
            }
        }

        for (int col = 0; col < SIZE; col++)
        {
            bool full = true;

            for (int row = 0; row < SIZE; row++)
            {
                if (!occupied[col, row])
                {
                    full = false;
                    break;
                }
            }

            fullCols[col] = full;

            if (full)
            {
                completedLines++;

                for (int row = 0; row < SIZE; row++)
                {
                    if (placedBlocks[col, row] != null)
                        blocksToRemove.Add(placedBlocks[col, row]);
                }
            }
        }

        if (completedLines == 0)
            return;

        foreach (GameObject block in blocksToRemove)
        {
            if (block != null)
            {
                SpriteRenderer sr =
                    block.GetComponent<SpriteRenderer>();

                if (sr != null)
                {
                    CreateBreakEffect(
                        block.transform.position,
                        sr.color
                    );
                }

                Destroy(block);
            }
        }

        for (int col = 0; col < SIZE; col++)
        {
            for (int row = 0; row < SIZE; row++)
            {
                if (fullRows[row] || fullCols[col])
                {
                    occupied[col, row] = false;
                    placedBlocks[col, row] = null;
                }
            }
        }

        score += completedLines * 10;
        UpdateHighScore();

        Debug.Log(
            "Da xoa " + completedLines +
            " hang/cot. Tong diem: " + score
        );
    }

    public void RestartGame()
    {
        score = 0;
        gameOver = false;

        occupied = new bool[SIZE, SIZE];
        placedBlocks = new GameObject[SIZE, SIZE];

        ClearPlacementPreview();

        foreach (GameObject piece in currentPieces)
        {
            if (piece != null)
                Destroy(piece);
        }

        currentPieces.Clear();

        if (boardRoot != null)
            Destroy(boardRoot.gameObject);

        CreateBoard();
        CreatePieces();
    }

    void InitStyles()
    {
        if (titleStyle != null)
            return;

        titleStyle = new GUIStyle
        {
            alignment = TextAnchor.MiddleCenter,
            fontSize = 26,
            fontStyle = FontStyle.Bold
        };
        titleStyle.normal.textColor = Color.white;

        headingStyle = new GUIStyle
        {
            alignment = TextAnchor.MiddleLeft,
            fontSize = 13,
            fontStyle = FontStyle.Bold
        };
        headingStyle.normal.textColor =
            new Color(0.65f, 0.75f, 0.95f);

        scoreStyle = new GUIStyle
        {
            alignment = TextAnchor.MiddleLeft,
            fontSize = 24,
            fontStyle = FontStyle.Bold
        };
        scoreStyle.normal.textColor = Color.white;

        hintStyle = new GUIStyle
        {
            alignment = TextAnchor.MiddleCenter,
            fontSize = 15
        };
        hintStyle.normal.textColor =
            new Color(0.75f, 0.82f, 0.96f);

        gameOverStyle = new GUIStyle
        {
            alignment = TextAnchor.MiddleCenter,
            fontSize = 34,
            fontStyle = FontStyle.Bold
        };
        gameOverStyle.normal.textColor = Color.white;

        buttonStyle = new GUIStyle(GUI.skin.button)
        {
            fontSize = 20,
            fontStyle = FontStyle.Bold,
            alignment = TextAnchor.MiddleCenter
        };
    }

    void DrawPanel(Rect rect, Color color)
    {
        Color old = GUI.color;
        GUI.color = color;
        GUI.DrawTexture(rect, Texture2D.whiteTexture);
        GUI.color = old;
    }

    void OnGUI()
    {
        InitStyles();

        float w = Screen.width;
        float h = Screen.height;

        float scale = Mathf.Clamp(w / 420f, 0.8f, 1.25f);

        titleStyle.fontSize = Mathf.RoundToInt(25 * scale);
        headingStyle.fontSize = Mathf.RoundToInt(13 * scale);
        scoreStyle.fontSize = Mathf.RoundToInt(24 * scale);
        hintStyle.fontSize = Mathf.RoundToInt(15 * scale);
        gameOverStyle.fontSize = Mathf.RoundToInt(34 * scale);
        buttonStyle.fontSize = Mathf.RoundToInt(20 * scale);

        GUI.Label(
            new Rect(0f, h * 0.012f, w, h * 0.045f),
            "COLOR BLOCKS",
            titleStyle
        );

        float margin = w * 0.05f;
        float gap = w * 0.03f;
        float cardW = (w - margin * 2f - gap) / 2f;
        float cardY = h * 0.065f;
        float cardH = h * 0.105f;

        DrawPanel(
            new Rect(margin, cardY, cardW, cardH),
            new Color(0.12f, 0.15f, 0.25f, 0.96f)
        );

        float recordX = margin + cardW + gap;

        DrawPanel(
            new Rect(recordX, cardY, cardW, cardH),
            new Color(0.12f, 0.15f, 0.25f, 0.96f)
        );

        GUI.Label(
            new Rect(
                margin + 8f,
                cardY + cardH * 0.08f,
                cardW - 16f,
                cardH * 0.34f
            ),
            "DIEM HIEN TAI",
            headingStyle
        );

        GUI.Label(
            new Rect(
                margin + 8f,
                cardY + cardH * 0.40f,
                cardW - 16f,
                cardH * 0.53f
            ),
            score.ToString(),
            scoreStyle
        );

        GUI.Label(
            new Rect(
                recordX + 8f,
                cardY + cardH * 0.08f,
                cardW - 16f,
                cardH * 0.34f
            ),
            "KY LUC",
            headingStyle
        );

        GUI.Label(
            new Rect(
                recordX + 8f,
                cardY + cardH * 0.40f,
                cardW - 16f,
                cardH * 0.53f
            ),
            highScore.ToString(),
            scoreStyle
        );

        GUI.Label(
            new Rect(0f, h * 0.94f, w, h * 0.045f),
            "KEO CAC KHOI VAO BAN CO",
            hintStyle
        );

        if (gameOver)
        {
            DrawPanel(
                new Rect(0f, 0f, w, h),
                new Color(0.02f, 0.03f, 0.08f, 0.88f)
            );

            GUI.Label(
                new Rect(0f, h * 0.29f, w, h * 0.09f),
                "GAME OVER",
                gameOverStyle
            );

            GUI.Label(
                new Rect(0f, h * 0.41f, w, h * 0.07f),
                "DIEM CUA BAN: " + score,
                scoreStyle
            );

            GUI.Label(
                new Rect(0f, h * 0.49f, w, h * 0.06f),
                "KY LUC: " + highScore,
                hintStyle
            );

            if (GUI.Button(
                new Rect(
                    w * 0.25f,
                    h * 0.61f,
                    w * 0.5f,
                    h * 0.075f
                ),
                "CHOI LAI",
                buttonStyle))
            {
                RestartGame();
            }
        }
    }
}

public class BlockPiece : MonoBehaviour
{
    public BlockBlastGame game;

    private Camera cam;
    private bool dragging;
    private bool draggingWithTouch;
    private bool placed;

    private Vector3 dragOffset;
    private Vector3 homePosition;

    void Awake()
    {
        homePosition = transform.position;
    }

    public void MarkPlaced()
    {
        placed = true;
    }

    public void ReturnToStart()
    {
        dragging = false;
        draggingWithTouch = false;

        if (!placed)
            transform.position = homePosition;
    }

    void Update()
    {
        if (game == null || !game.enabled ||
            game.IsGameOver || placed)
            return;

        if (cam == null)
            cam = Camera.main;

        if (cam == null)
            return;

        Vector2 pointerPosition = Vector2.zero;

        bool pressedThisFrame = false;
        bool isPressed = false;
        bool releasedThisFrame = false;

        if (dragging && draggingWithTouch)
        {
            if (Touchscreen.current != null)
            {
                var touch = Touchscreen.current.primaryTouch;

                pointerPosition = touch.position.ReadValue();
                isPressed = touch.press.isPressed;

                releasedThisFrame =
                    touch.press.wasReleasedThisFrame;
            }
        }
        else if (dragging)
        {
            if (Mouse.current != null)
            {
                pointerPosition =
                    Mouse.current.position.ReadValue();

                isPressed = Mouse.current.leftButton.isPressed;

                releasedThisFrame =
                    Mouse.current.leftButton.wasReleasedThisFrame;
            }
        }
        else
        {
            if (Touchscreen.current != null)
            {
                var touch = Touchscreen.current.primaryTouch;

                if (touch.press.wasPressedThisFrame)
                {
                    pointerPosition = touch.position.ReadValue();
                    pressedThisFrame = true;
                    draggingWithTouch = true;
                }
            }

            if (!pressedThisFrame && Mouse.current != null &&
                Mouse.current.leftButton.wasPressedThisFrame)
            {
                pointerPosition =
                    Mouse.current.position.ReadValue();

                pressedThisFrame = true;
                draggingWithTouch = false;
            }
        }

        Vector3 pointerWorld = cam.ScreenToWorldPoint(
            new Vector3(
                pointerPosition.x,
                pointerPosition.y,
                -cam.transform.position.z
            )
        );

        pointerWorld.z = 0f;

        if (pressedThisFrame)
        {
            foreach (SpriteRenderer sr in
                     GetComponentsInChildren<SpriteRenderer>())
            {
                if (sr.gameObject == gameObject)
                    continue;

                if (sr.bounds.Contains(pointerWorld))
                {
                    dragging = true;
                    dragOffset = transform.position - pointerWorld;
                    break;
                }
            }
        }

        if (dragging && isPressed)
        {
            Vector3 target = pointerWorld + dragOffset;

            transform.position = Vector3.Lerp(
                transform.position,
                target,
                Mathf.Clamp01(Time.deltaTime * 25f)
            );

            game.UpdatePlacementPreview(gameObject);
        }

        if (dragging && releasedThisFrame)
        {
            dragging = false;

            transform.position = pointerWorld + dragOffset;

            game.ClearPlacementPreview();
            game.SnapAndPlace(gameObject);
        }

        if (dragging && !isPressed &&
            !pressedThisFrame && !releasedThisFrame)
        {
            ReturnToStart();
            game.ClearPlacementPreview();
        }
    }
}
